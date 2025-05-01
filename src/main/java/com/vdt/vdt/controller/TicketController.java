package com.vdt.vdt.controller;

import com.vdt.vdt.dto.CreateTicketRequest;
import com.vdt.vdt.dto.CreateTicketResponse;
import com.vdt.vdt.dto.SlaBreachStatusDTO;
import com.vdt.vdt.dto.TicketDetailDto;
import com.vdt.vdt.entity.TicketPriority;
import com.vdt.vdt.entity.TicketStatus;
import com.vdt.vdt.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<CreateTicketResponse> createTicket(@RequestBody CreateTicketRequest request) {
        try {
            CreateTicketResponse ticketResponse = ticketService.createTicket(request);
            return ResponseEntity.ok(ticketResponse);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @PutMapping("/{id}/tickets/update-status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            String updateMessage = ticketService.updateStatus(id, status.toUpperCase());
            return ResponseEntity.ok(updateMessage);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @GetMapping("/status/{ticketId}")
    public ResponseEntity<String> checkTicketStatus(@PathVariable Long ticketId) {
        try {
            String status = ticketService.checkTicketStatus(ticketId);
            return ResponseEntity.ok(status);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @PostMapping("add-comment/{id}")
    public ResponseEntity<Void> addComment(@PathVariable Long id, @RequestParam String authorName, @RequestBody String comment) {
        try {
            ticketService.addComment(id, comment, authorName);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketDetailDto> getTicketDetails(@PathVariable Long ticketId) {
        try {
            TicketDetailDto dto = ticketService.getTicketDetails(ticketId);
            return ResponseEntity.ok(dto);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<TicketDetailDto>> getAllCustomerTickets(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<TicketDetailDto> ticketDtos = ticketService.getAllTicketsForCustomer(customerId, pageable);
            return ResponseEntity.ok(ticketDtos);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }


    @GetMapping("/search")
    public ResponseEntity<Page<TicketDetailDto>> searchTickets(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long ticketId,
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) TicketPriority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<TicketDetailDto> ticketDtos = ticketService.searchTickets(customerId, ticketId, status, priority, page, size);
            return ResponseEntity.ok(ticketDtos);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @GetMapping("/dashboard-stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        try {
            Map<String, Object> stats = ticketService.getDashboardStats();
            return ResponseEntity.ok(stats);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @PostMapping("/{ticketId}/respond")
    public ResponseEntity<String> respondToTicket(
            @PathVariable Long ticketId,
            @RequestParam Long agentId,
            @RequestBody String responseText) {
        try {
            String result = ticketService.respondToTicket(ticketId, agentId, responseText);
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @PostMapping("/{ticketId}/pause-sla")
    public ResponseEntity<String> pauseSlaTimer(@PathVariable Long ticketId, @RequestParam String reason) {
        try {
            ticketService.pauseSlaTimer(ticketId, reason);
            return ResponseEntity.ok("SLA timer paused successfully");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @PostMapping("/{ticketId}/resume-sla")
    public ResponseEntity<String> resumeSlaTimer(@PathVariable Long ticketId) {
        try {
            ticketService.resumeSlaTimer(ticketId);
            return ResponseEntity.ok("SLA timer resumed successfully");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @GetMapping("/{ticketId}/sla-status")
    public ResponseEntity<SlaBreachStatusDTO> getSlaBreachStatus(@PathVariable Long ticketId) {
        try {
            SlaBreachStatusDTO slaStatus = ticketService.getSlaBreachStatus(ticketId);
            return ResponseEntity.ok(slaStatus);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @PostMapping("/{ticketId}/assign")
    public ResponseEntity<String> assignTicket(@PathVariable Long ticketId,
                                               @RequestParam String agentEmail) {
        try {
            String response = ticketService.assignTicket(ticketId, agentEmail);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @PostMapping("/{ticketId}/start-work")
    public ResponseEntity<String> startWorkOnTicket(@PathVariable Long ticketId) {
        try {
            String response = ticketService.startWorkOnTicket(ticketId);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @PostMapping("/{ticketId}/complete")
    public ResponseEntity<String> completeTicket(@PathVariable Long ticketId) {
        try {
            String response = ticketService.completeTicket(ticketId);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @PostMapping("/{ticketId}/close")
    public ResponseEntity<String> closeTicket(@PathVariable Long ticketId) {
        try {
            String response = ticketService.closeTicket(ticketId);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }
}
