package com.vdt.vdt.service;

import com.vdt.vdt.dto.CreateTicketRequest;
import com.vdt.vdt.dto.CreateTicketResponse;
import com.vdt.vdt.dto.SlaBreachStatusDTO;
import com.vdt.vdt.dto.TicketDetailDto;
import com.vdt.vdt.entity.*;
import com.vdt.vdt.repository.CaseRepository;
import com.vdt.vdt.repository.SlaPolicyRepository;
import com.vdt.vdt.repository.TicketCommentRepository;
import com.vdt.vdt.repository.TicketRepository;
import com.vdt.vdt.specification.TicketSpecification;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
@Slf4j
public class TicketService {
    @Autowired
    private TicketCommentRepository ticketCommentRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ModelMapper ticketMapper;

    @Autowired
    private SlaPolicyRepository slaPolicyRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AsyncNotificationService asyncNotificationService;

    @Autowired
    private CaseRepository caseRepository;


    public CreateTicketResponse createTicket(CreateTicketRequest request) {
        Customer customer = customerService.findById(request.getCustomerId());

        Ticket ticket = ticketMapper.map(request, Ticket.class);
        ticket.setCustomer(customer);
        ticket.setCreatedBy(customer.getId());
        ticket.setCreatedAt(LocalDateTime.now());
        if(request.getAgentEmail() != null) {
            Optional<User> agent = userService.findByEmail(request.getAgentEmail());
            if(agent.isEmpty()) {
                throw new IllegalArgumentException("Agent not found");
            }
            ticket.setAssignedAgent(agent.get());
            ticket.setAssignedDepartment(agent.get().getDepartment());
        }
        SlaPolicy slaPolicy = slaPolicyRepository.findMatchingPolicy(
                TicketType.valueOf(request.getType()),
                TicketPriority.valueOf(request.getPriority()),
                customer.getAccountType()
        ).orElseThrow(() -> new IllegalArgumentException("No SLA Policy found"));

        ticket.setSlaResponseDueAt(
                ticket.getCreatedAt().plus(Duration.ofMinutes(slaPolicy.getResponseTimeTargetInMinutes()))
        );

        ticket.setSlaResolutionDueAt(
                ticket.getCreatedAt().plus(Duration.ofMinutes(slaPolicy.getResolutionTimeTargetInMinutes()))
        );

        if (request.getCaseId() != null) {
            Case parentCase = caseRepository.findById(request.getCaseId())
                    .orElseThrow(() -> new IllegalArgumentException("Case not found"));
            ticket.setTicketCase(parentCase);
        }
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setUpdatedAt(LocalDateTime.now());
        Ticket savedTicket = ticketRepository.save(ticket);


        handleHighPriorityTicket(savedTicket);

        groupTicketIntoExistingCase(savedTicket);

        CreateTicketResponse response = ticketMapper.map(savedTicket, CreateTicketResponse.class);
        response.setCustomerEmail(customer.getEmail());
        response.setCustomerName(customer.getName());
        return response;
    }


    public String respondToTicket(Long ticketId, Long agentId, String response) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (ticket.getFirstResponseAt() != null) {
            throw new IllegalStateException("Ticket already responded to");
        }

        User agent = userService.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        if (!ticket.getAssignedAgent().getId().equals(agent.getId())) {
            throw new RuntimeException("This agent is not assigned to this ticket");
        }

        LocalDateTime now = LocalDateTime.now();
        ticket.setFirstResponseAt(now);
        ticket.setLastActionAt(now);
        ticket.setResponseTimeMinutes(
                Duration.between(ticket.getCreatedAt().toInstant(ZoneOffset.UTC), Instant.now()).toMinutes()
        );
        if (ticket.getSlaResponseDueAt() != null && now.isAfter(ticket.getSlaResponseDueAt())) {
            ticket.setResponseSlaBreached(true);
        }

        ticketRepository.save(ticket);
        TicketComment comment = new TicketComment();
        comment.setTicket(ticket);
        comment.setAuthorName(agent.getEmail());
        comment.setComment(response);
        comment.setTimestamp(LocalDateTime.now());
        ticket.getComments().add(comment);
        comment.setTicketId(ticket.getId());
        ticketCommentRepository.save(comment);


        return ticket.isResponseSlaBreached()
                ? "Response recorded. SLA for response was breached."
                : "Response recorded successfully within SLA.";
    }

    public String assignTicket(Long ticketId, String agentEmail) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
        Optional<User> assignedAgent = userService.findByEmail(agentEmail);
        if (assignedAgent.isEmpty()) {
            throw new IllegalArgumentException("Assigned agent not found");
        }
        if (ticket.getStatus() != TicketStatus.OPEN) {
            throw new IllegalArgumentException("Ticket must be open to be assigned");
        }
        if (ticket.getAssignedAgent() != null) {
            throw new IllegalArgumentException("Ticket already assigned");
        }
        ticket.setAssignedAgent(assignedAgent.get());
        ticket.setStatus(TicketStatus.ASSIGNED);
        ticket.setLastActionAt(LocalDateTime.now());
        if(ticket.getFirstResponseAt() == null){
            ticket.setFirstResponseAt(LocalDateTime.now());
        }

        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setAssignedDepartment(assignedAgent.get().getDepartment());
        Ticket savedTicket = ticketRepository.save(ticket);
        asyncNotificationService.notifyAgent(ticket.getAssignedAgent(), savedTicket.getId());
        return "ticket has been assigned to " + savedTicket.getAssignedAgent().getEmail();

    }
    public String checkTicketStatus(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        return String.format("Ticket ID %d is currently %s", ticketId, ticket.getStatus());
    }

    public String startWorkOnTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        if (ticket.getStatus() != TicketStatus.ASSIGNED) {
            throw new IllegalStateException("Ticket must be assigned before starting work");
        }

        ticket.setStatus(TicketStatus.IN_PROGRESS);
        ticket.setTicketSlaStatus(TicketSlaStatus.ON_TRACK);
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setLastActionAt(LocalDateTime.now());
        ticketRepository.save(ticket);

        return "Ticket work started successfully, status is now IN_PROGRESS";
    }

    public String completeTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        if (ticket.getStatus() != TicketStatus.IN_PROGRESS) {
            throw new IllegalStateException("Ticket must be in progress before completion");
        }

        ticket.setStatus(TicketStatus.RESOLVED);
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setResolvedAt(LocalDateTime.now());
        ticket.setLastActionAt(LocalDateTime.now());
        ticketRepository.save(ticket);
        return "Ticket completed successfully";
    }

    public String closeTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        if (ticket.getStatus() != TicketStatus.RESOLVED) {
            throw new IllegalStateException("Ticket must be resolved before closing");
        }

        ticket.setStatus(TicketStatus.CLOSED);
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setLastActionAt(LocalDateTime.now());
        ticket.setResolutionTimeMinutes(
                Duration.between(ticket.getCreatedAt().toInstant(ZoneOffset.UTC), Instant.now()).toMinutes()
        );
        ticketRepository.save(ticket);

        return "Ticket closed successfully";
    }

    public String updateStatus(Long ticketId, String newStatus) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        ticket.setStatus(TicketStatus.valueOf(newStatus));
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setLastActionAt(LocalDateTime.now());
        ticketRepository.save(ticket);
        return String.format("Ticket has been updated to %s", newStatus);

    }

    public void addComment(Long ticketId, String comment, String authorName) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        TicketComment ticketComment = new TicketComment();
        ticketComment.setComment(comment);
        ticketComment.setAuthorName(authorName);
        ticketComment.setTimestamp(LocalDateTime.now());
        ticketComment.setTicket(ticket);
        ticket.getComments().add(ticketComment);
        ticketComment.setTicketId(ticket.getId());
        ticket.setLastActionAt(LocalDateTime.now());
        ticketRepository.save(ticket);
    }


    public Ticket getTicketById(Long ticketId) {
        return ticketRepository.findById(ticketId).orElseThrow(
                () -> new RuntimeException("Ticket not found"));
    }

    public TicketDetailDto getTicketDetails(Long ticketId) {
        Ticket ticket = getTicketById(ticketId);
        return ticketMapper.map(ticket, TicketDetailDto.class);
    }


    public Page<TicketDetailDto> getAllTicketsForCustomer(Long customerId, Pageable pageable) {
        Page<Ticket> ticketsPage = ticketRepository.findByCustomerId(customerId, pageable);
        return ticketsPage.map(ticket -> ticketMapper.map(ticket, TicketDetailDto.class));
    }



    public Page<TicketDetailDto> searchTickets(Long customerId, Long ticketId, TicketStatus status, TicketPriority priority, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<Ticket> specification = TicketSpecification.combineSpecifications(customerId, ticketId, status, priority);
        Page<Ticket> ticketPage = ticketRepository.findAll(specification, pageable);
        return ticketPage.map(ticket -> ticketMapper.map(ticket, TicketDetailDto.class));
    }


    public Map<String, Object> getDashboardStats() {
        Map<String, Object> dashboardStats = new HashMap<>();


        long totalTickets = ticketRepository.count();


        long openTickets = ticketRepository.countByStatus(TicketStatus.OPEN);
        long inProgressTickets = ticketRepository.countByStatus(TicketStatus.IN_PROGRESS);
        long escalatedTickets = ticketRepository.countByStatus(TicketStatus.ESCALATED);
        long closedTickets = ticketRepository.countByStatus(TicketStatus.RESOLVED);


        double openPercentage = totalTickets > 0 ? ((double) openTickets / totalTickets) * 100 : 0;
        double inProgressPercentage = totalTickets > 0 ? ((double) inProgressTickets / totalTickets) * 100 : 0;
        double escalatedPercentage = totalTickets > 0 ? ((double) escalatedTickets / totalTickets) * 100 : 0;
        double closedPercentage = totalTickets > 0 ? ((double) closedTickets / totalTickets) * 100 : 0;


        dashboardStats.put("totalTickets", totalTickets);
        dashboardStats.put("openTickets", openTickets);
        dashboardStats.put("openPercentage", openPercentage);
        dashboardStats.put("inProgressTickets", inProgressTickets);
        dashboardStats.put("inProgressPercentage", inProgressPercentage);
        dashboardStats.put("escalatedTickets", escalatedTickets);
        dashboardStats.put("escalatedPercentage", escalatedPercentage);
        dashboardStats.put("closedTickets", closedTickets);
        dashboardStats.put("closedPercentage", closedPercentage);

        return dashboardStats;
    }

    public void pauseSlaTimer(Long ticketId, String reason) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));


        ticket.setSlaTimerPaused(true);
        ticket.setPauseReason(reason);
        ticket.setSlaPausedAt(LocalDateTime.now());

        ticketRepository.save(ticket);
    }
    public void resumeSlaTimer(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));


        ticket.setSlaTimerPaused(false);
        ticket.setPauseReason(null);
        ticket.setSlaPausedAt(null);

        ticketRepository.save(ticket);
    }


    public Duration getTimeLeftBeforeSlaBreach(LocalDateTime slaDueAt) {
        LocalDateTime now = LocalDateTime.now();
        return Duration.between(now, slaDueAt);
    }


    public SlaBreachStatusDTO getSlaBreachStatus(Long ticketId) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
        if (ticketOpt.isPresent()) {
            Ticket ticket = ticketOpt.get();
            Duration remainingTime = getTimeLeftBeforeSlaBreach(ticket.getSlaResolutionDueAt());
            String formattedTime = formatRemainingTime(remainingTime);
            return new SlaBreachStatusDTO(formattedTime, ticket.getTicketSlaStatus().name());
        }

        return new SlaBreachStatusDTO("Ticket not found", "N/A");
    }

    private String formatRemainingTime(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        return String.format("%d hours %d minutes", hours, minutes);
    }

    private void handleHighPriorityTicket(Ticket ticket) {
        if (ticket.getPriority() == TicketPriority.HIGH) {
            Optional<Case> existing = caseRepository.findOpenCaseByIssueType(ticket.getIssueType());

            Case targetCase = existing.orElseGet(() -> {
                Case newCase = new Case();
                newCase.setCaseTitle("Auto-case for: " + ticket.getIssueType());
                newCase.setCaseStatus(TicketStatus.OPEN);
                newCase.setCaseType(CaseType.SLA_WATCH);
                return caseRepository.save(newCase);
            });

            ticket.setTicketCase(targetCase);
            ticketRepository.save(ticket);
        }
    }
    private void groupTicketIntoExistingCase(Ticket ticket) {
        IssueType issueType = ticket.getIssueType();
        Optional<Case> openCase = caseRepository.findOpenCaseByIssueType(issueType);

        Case parentCase = openCase.orElseGet(() -> {
            Case newCase = new Case();
            newCase.setCaseTitle(issueType.name().replace("_", " ") + " Case");
            newCase.setIssueType(issueType);
            newCase.setCaseStatus(TicketStatus.OPEN);
            newCase.setCreatedAt(LocalDateTime.now());
            return caseRepository.save(newCase);
        });

        ticket.setTicketCase(parentCase);
        ticketRepository.save(ticket);
    }



}
