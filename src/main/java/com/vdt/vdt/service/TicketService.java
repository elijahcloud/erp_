package com.vdt.vdt.service;

import com.vdt.vdt.dto.CreateTicketRequest;
import com.vdt.vdt.dto.TicketDetailDto;
import com.vdt.vdt.dto.TicketMapper;
import com.vdt.vdt.entity.*;
import com.vdt.vdt.repository.TicketRepository;
import com.vdt.vdt.specification.TicketSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TicketMapper ticketMapper;

    public String createTicket(CreateTicketRequest request) {
        Customer customer = customerService.findById(request.getCustomerId());

        Ticket ticket = ticketMapper.toEntity(request, customer);

         ticketRepository.save(ticket);
         return "Ticket created successfully";
    }

    public String updateStatus(Long ticketId, TicketStatus newStatus) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        ticket.setStatus(newStatus);
        ticket.setUpdatedAt(LocalDateTime.now());
        ticketRepository.save(ticket);
        return String.format("Ticket has been updated to %s", newStatus);

    }

    public void addComment(Long ticketId, String comment, String authorName) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        TicketComment c = new TicketComment();
        c.setComment(comment);
        c.setAuthorName(authorName);
        c.setTimestamp(LocalDateTime.now());
        c.setTicket(ticket);
        ticket.getComments().add(c);
        ticketRepository.save(ticket);
    }

    public Ticket getTicketById(Long ticketId) {
        return ticketRepository.findById(ticketId).orElseThrow(
                () -> new RuntimeException("Ticket not found"));
    }

    public TicketDetailDto getTicketDetails(Long ticketId) {
        Ticket ticket = getTicketById(ticketId);
       return ticketMapper.toDto(ticket);
    }
    public List<TicketDetailDto> getAllTicketsForCustomer(Long customerId) {

        List<Ticket> tickets = ticketRepository.findByCustomerId(customerId);
        return tickets.stream()
                .map(ticketMapper::toDto)
                .collect(Collectors.toList());
    }

    public Page<TicketDetailDto> searchTickets(Long customerId, Long ticketId, TicketStatus status, TicketPriority priority, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<Ticket> specification = TicketSpecification.combineSpecifications(customerId, ticketId, status, priority);


        Page<Ticket> ticketPage = ticketRepository.findAll(specification, pageable);


        return ticketPage.map(ticket -> new TicketDetailDto(ticket));
    }

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> dashboardStats = new HashMap<>();


        long totalTickets = ticketRepository.count();


        long openTickets = ticketRepository.countByStatus(TicketStatus.OPEN);
        long inProgressTickets = ticketRepository.countByStatus(TicketStatus.IN_PROGRESS);
        long escalatedTickets = ticketRepository.countByStatus(TicketStatus.ESCALATED);
        long closedTickets = ticketRepository.countByStatus(TicketStatus.CLOSED);


        double openPercentage = (totalTickets > 0) ? ((double) openTickets / totalTickets) * 100 : 0;
        double inProgressPercentage = (totalTickets > 0) ? ((double) inProgressTickets / totalTickets) * 100 : 0;
        double escalatedPercentage = (totalTickets > 0) ? ((double) escalatedTickets / totalTickets) * 100 : 0;
        double closedPercentage = (totalTickets > 0) ? ((double) closedTickets / totalTickets) * 100 : 0;


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
}

