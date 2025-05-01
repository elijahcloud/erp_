package com.vdt.vdt.controller;

import com.vdt.vdt.dto.SlaPolicyRequest;
import com.vdt.vdt.dto.SlaPolicyResponse;
import com.vdt.vdt.dto.TicketSlaDashboardDTO;
import com.vdt.vdt.dto.TicketStatisticsDto;
import com.vdt.vdt.entity.SlaPolicy;
import com.vdt.vdt.service.SlaPolicyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/sla-policies")

public class SlaPolicyController {

    private final SlaPolicyService slaPolicyService;

    public SlaPolicyController(SlaPolicyService slaPolicyService) {
        this.slaPolicyService = slaPolicyService;
    }

    @GetMapping
    public ResponseEntity<Page<SlaPolicyResponse>> getAllPolicies(Pageable pageable) {
        try {
            Page<SlaPolicyResponse> response = slaPolicyService.getAllPolicies(pageable);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<SlaPolicyResponse> getPolicyById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(slaPolicyService.getPolicyById(id));
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SlaPolicyResponse> createPolicy(@RequestBody SlaPolicyRequest request) {
        try {
            return ResponseEntity.ok(slaPolicyService.createPolicy(request));
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SlaPolicyResponse> updatePolicy(@PathVariable Long id, @RequestBody SlaPolicyRequest request) {
        try {
            return ResponseEntity.ok(slaPolicyService.updatePolicy(id, request));
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePolicy(@PathVariable Long id) {
        try {
            slaPolicyService.deletePolicy(id);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @GetMapping("/sla-compliance")
    public ResponseEntity<Double> calculateSlaCompliancePercentageForTicket(@RequestParam Long ticketId) {
        try {
            double result = slaPolicyService.calculateSlaCompliancePercentageForTicket(ticketId);
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @GetMapping("/customer/{customerId}/dashboard")
    public ResponseEntity<Page<TicketSlaDashboardDTO>> getCustomerTicketsDashboard(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {

            Page<TicketSlaDashboardDTO> dashboard = slaPolicyService.getTicketsForCustomer(customerId, page, size);
            System.out.println("i got here");
            return ResponseEntity.ok(dashboard);
        } catch (Exception ex) {
           throw new RuntimeException(ex.getMessage());

        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Page<TicketSlaDashboardDTO>> getAllTicketsDashboard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<TicketSlaDashboardDTO> dashboard = slaPolicyService.getAllTickets(page, size);
            return ResponseEntity.ok(dashboard);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @GetMapping("/customer/{customerId}/statistics")
    public ResponseEntity<TicketStatisticsDto> getCustomerStatistics(@PathVariable Long customerId) {
        try {
            TicketStatisticsDto stats = slaPolicyService.getStatisticsForCustomer(customerId);
            return ResponseEntity.ok(stats);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @GetMapping("/statistics")
    public ResponseEntity<TicketStatisticsDto> getAllStatistics() {
        try {
            TicketStatisticsDto stats = slaPolicyService.getStatisticsForAllTickets();
            return ResponseEntity.ok(stats);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @GetMapping("/ticket/{ticketId}/policy")
    public ResponseEntity<SlaPolicy> getSlaPolicyForTicket(@PathVariable Long ticketId) {
        try {
            return ResponseEntity.ok(slaPolicyService.getSlaPolicyForTicket(ticketId));
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}