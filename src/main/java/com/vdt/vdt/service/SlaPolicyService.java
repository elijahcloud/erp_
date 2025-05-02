package com.vdt.vdt.service;

import com.vdt.vdt.dto.SlaPolicyRequest;
import com.vdt.vdt.dto.SlaPolicyResponse;
import com.vdt.vdt.dto.TicketSlaDashboardDTO;
import com.vdt.vdt.dto.TicketStatisticsDto;
import com.vdt.vdt.entity.*;
import com.vdt.vdt.repository.SlaPolicyRepository;
import com.vdt.vdt.repository.TicketRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class SlaPolicyService {

    private final SlaPolicyRepository slaPolicyRepository;
    private final ModelMapper slaPolicyMapper;
    private final TicketRepository ticketRepository;
    private static final Logger log = LoggerFactory.getLogger(SlaPolicyService.class);

    public SlaPolicyService(SlaPolicyRepository slaPolicyRepository, ModelMapper slaPolicyMapper, TicketRepository ticketRepository) {
        this.slaPolicyRepository = slaPolicyRepository;
        this.slaPolicyMapper = slaPolicyMapper;
        this.ticketRepository = ticketRepository;
    }

    public Page<SlaPolicyResponse> getAllPolicies(Pageable pageable) {
        Page<SlaPolicy> policies = slaPolicyRepository.findAllByDeletedFalse(pageable);
        return policies.map(policy -> slaPolicyMapper.map(policy, SlaPolicyResponse.class));
    }


    public SlaPolicyResponse createPolicy(SlaPolicyRequest request) {
        SlaPolicy slaPolicy = slaPolicyMapper.map(request,SlaPolicy.class);
        slaPolicy.setTicketType(TicketType.valueOf(request.getTicketType()));
        slaPolicy.setCreatedAt(LocalDateTime.now());
        slaPolicy.setUpdatedAt(LocalDateTime.now());
        SlaPolicy saved = slaPolicyRepository.save(slaPolicy);
        return slaPolicyMapper.map(saved,SlaPolicyResponse.class);
    }

    public SlaPolicyResponse updatePolicy(Long id, SlaPolicyRequest request) {
        SlaPolicy slaPolicy = slaPolicyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SLA Policy with id " + id + " not found"));

        applyPartialUpdate(slaPolicy, request);

        slaPolicy.setUpdatedAt(LocalDateTime.now());
        SlaPolicy updated = slaPolicyRepository.save(slaPolicy);

        return slaPolicyMapper.map(updated, SlaPolicyResponse.class);
    }


    private void applyPartialUpdate(SlaPolicy slaPolicy, SlaPolicyRequest request) {
        Optional.ofNullable(request.getTicketType())
                .map(String::toUpperCase)
                .map(TicketType::valueOf)
                .ifPresent(slaPolicy::setTicketType);

        Optional.ofNullable(request.getPriority())
                .map(String::toUpperCase)
                .map(TicketPriority::valueOf)
                .ifPresent(slaPolicy::setPriority);

        Optional.ofNullable(request.getCustomerGroup())
                .map(String::toUpperCase)
                .map(CustomerAccountType::valueOf)
                .ifPresent(slaPolicy::setCustomerGroup);
        if (request.getResponseTimeTargetInMinutes() > 0) {
            slaPolicy.setResponseTimeTargetInMinutes(request.getResponseTimeTargetInMinutes());
        }


        if (request.getResolutionTimeTargetInMinutes() > 0) {
            slaPolicy.setResolutionTimeTargetInMinutes(request.getResolutionTimeTargetInMinutes());
        }

        Optional.ofNullable(request.getReassignThresholdHours())
                .ifPresent(slaPolicy::setReassignThresholdHours);

    }

    public void deletePolicy(Long id) {
        SlaPolicy slaPolicy = slaPolicyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SLA Policy with id " + id + " not found"));

        slaPolicy.setDeleted(true);
        slaPolicy.setUpdatedAt(LocalDateTime.now());

        slaPolicyRepository.save(slaPolicy);
    }

    public SlaPolicy getSlaPolicyForTicket(long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket with id " + ticketId + " not found"));
        return slaPolicyRepository.findByCustomerGroupAndTicketType(ticket.getCustomer().getAccountType(), ticket.getTicketType());
    }

    public SlaPolicyResponse getPolicyById(Long id) {
        SlaPolicy slaPolicy = slaPolicyRepository.findById(id)
                .filter(policy -> !policy.isDeleted())
                .orElseThrow(() -> new RuntimeException("SLA Policy with ID " + id + " not found or has been deleted"));

        return slaPolicyMapper.map(slaPolicy, SlaPolicyResponse.class);
    }

    public double calculateSlaCompliancePercentageForTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket with id " + ticketId + " not found"));

        SlaPolicy slaPolicy = slaPolicyRepository.findByCustomerGroupAndTicketType(
                ticket.getCustomer().getAccountType(),
                ticket.getTicketType()
        );

        if (slaPolicy == null) {
            throw new RuntimeException("No SLA Policy found for this ticket's type and customer group");
        }

        int complianceScore = 0;
        int totalChecks = 0;


        if (ticket.getFirstResponseAt() != null) {
            totalChecks++;
            Duration actualResponseTime = Duration.between(ticket.getCreatedAt(), ticket.getFirstResponseAt());
            if (actualResponseTime.toMinutes() <= slaPolicy.getResponseTimeTargetInMinutes()) {
                complianceScore++;
            }
        }


        if (ticket.getResolvedAt() != null) {
            totalChecks++;
            Duration actualResolutionTime = Duration.between(ticket.getCreatedAt(), ticket.getResolvedAt());
            if (actualResolutionTime.toMinutes() <= slaPolicy.getResolutionTimeTargetInMinutes()) {
                complianceScore++;
            }
        }

        if (totalChecks == 0) return 0.0;

        return ((double) complianceScore / totalChecks) * 100;
    }

    public Page<TicketSlaDashboardDTO> getTicketsForCustomer(Long customerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Ticket> tickets = ticketRepository.findByCustomerId(customerId, pageable);
        return tickets.map(this::convertToTicketSlaDashboardDTO);
    }

    public Page<TicketSlaDashboardDTO> getAllTickets(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Ticket> tickets = ticketRepository.findAll(pageable);
        return tickets.map(this::convertToTicketSlaDashboardDTO);
    }

    public TicketStatisticsDto getStatisticsForCustomer(Long customerId) {
        List<Ticket> tickets = ticketRepository.findByCustomerId(customerId);
        return calculateTicketStatistics(tickets);
    }

    public TicketStatisticsDto getStatisticsForAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        return calculateTicketStatistics(tickets);
    }

    private TicketStatisticsDto calculateTicketStatistics(List<Ticket> tickets) {
        TicketStatisticsDto statisticsDto = new TicketStatisticsDto();

        statisticsDto.setTotalTickets(tickets.size());

        long slaCompliantCount = tickets.stream()
                .filter(this::isSlaCompliant)
                .count();
        double slaCompliance = tickets.isEmpty() ? 0 : (slaCompliantCount / (double) tickets.size()) * 100;
        statisticsDto.setSlaCompliance(slaCompliance);

        double avgResponseTime = tickets.stream()
                .mapToLong(Ticket::getResponseTimeMinutes)
                .average()
                .orElse(0);
        statisticsDto.setAvgResponseTime(avgResponseTime);

        double avgResolutionTime = tickets.stream()
                .mapToLong(Ticket::getResolutionTimeMinutes)
                .average()
                .orElse(0);
        statisticsDto.setAvgResolutionTime(avgResolutionTime);

        return statisticsDto;
    }

    private boolean isSlaCompliant(Ticket ticket) {
        if (ticket.getCreatedAt() == null || ticket.getResolvedAt() == null || ticket.getSlaResolutionDueAt() == null) {
            return false;
        }

        return !ticket.getResolvedAt().isAfter(ticket.getSlaResolutionDueAt());
    }

    private TicketSlaDashboardDTO convertToTicketSlaDashboardDTO(Ticket ticket) {
        TicketSlaDashboardDTO dto = slaPolicyMapper.map(ticket, TicketSlaDashboardDTO.class);
        String resolutionTime;
        if (ticket.getResolvedAt() == null && ticket.getStatus() != TicketStatus.CLOSED) {
            long remainingTime = ticket.getSlaResolutionDueAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - System.currentTimeMillis();
           resolutionTime = remainingTime > 0
                    ? "about " + remainingTime / 1000 / 60 / 60 + " hours remaining"
                    : "Overdue by about " + Math.abs(remainingTime) / 1000 / 60 / 60 + " hours";

        } else resolutionTime = "Resolved";

        dto.setSlaType(ticket.getCustomer().getAccountType().toString());
        dto.setResolutionTime(resolutionTime);
        dto.setProgress(calculateProgress(ticket.getStatus()));
        dto.setResponse(ticket.getFirstResponseAt() != null ? "Responded" : "Not Responded");
        return dto;
    }
    public String calculateProgress(TicketStatus status) {
        if (status == null) return "0%";

        return switch (status) {
            case OPEN -> "0%";
            case ASSIGNED -> "20%";
            case IN_PROGRESS -> "50%";
            case ESCALATED -> "75%";
            case RESOLVED, CLOSED -> "100%";
        };
    }



}
