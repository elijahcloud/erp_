package com.vdt.vdt.controller;

import com.vdt.vdt.dto.CreateTicketRequest;
import com.vdt.vdt.dto.TicketDetailDto;
import com.vdt.vdt.entity.Ticket;
import com.vdt.vdt.entity.TicketPriority;
import com.vdt.vdt.entity.TicketStatus;
import com.vdt.vdt.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<String> createTicket(@RequestBody CreateTicketRequest request) {
        String ticketResponse = ticketService.createTicket(request);
        return ResponseEntity.ok(ticketResponse);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id, @RequestParam TicketStatus status) {
        String updateMessage = ticketService.updateStatus(id, status);
        return ResponseEntity.ok(updateMessage);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Void> addComment(@PathVariable Long id, @RequestParam String authorName, @RequestBody String comment) {
        ticketService.addComment(id, comment, authorName);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketDetailDto> getTicketDetails(@PathVariable Long ticketId) {
        TicketDetailDto dto = ticketService.getTicketDetails(ticketId);

        return ResponseEntity.ok(dto);
    }
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TicketDetailDto>> getAllCustomerTickets(@PathVariable Long customerId) {
        List<TicketDetailDto> ticketDtos = ticketService.getAllTicketsForCustomer(customerId);
        return ResponseEntity.ok(ticketDtos);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<TicketDetailDto>> searchTickets(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long ticketId,
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) TicketPriority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<TicketDetailDto> ticketDtos = ticketService.searchTickets(customerId, ticketId, status, priority, page, size);

        return ResponseEntity.ok(ticketDtos);
    }

    @GetMapping("/dashboard-stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = ticketService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }
}
