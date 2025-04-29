package com.vdt.vdt.controller;

import com.vdt.vdt.dto.SlaPolicyRequest;
import com.vdt.vdt.dto.SlaPolicyResponse;
import com.vdt.vdt.entity.SlaPolicy;
import com.vdt.vdt.entity.Ticket;
import com.vdt.vdt.service.SlaPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sla-policies")

public class SlaPolicyController {

    private final SlaPolicyService slaPolicyService;

    private SlaPolicyController(SlaPolicyService slaPolicyService) {
        this.slaPolicyService = slaPolicyService;
    }

    @GetMapping
    public List<SlaPolicyResponse> getAllPolicies() {
        return slaPolicyService.getAllPolicies();
    }

    @PostMapping
    public SlaPolicyResponse createPolicy(@RequestBody SlaPolicyRequest request) {
        return slaPolicyService.createPolicy(request);
    }

    @PutMapping("/{id}")
    public SlaPolicyResponse updatePolicy(@PathVariable Long id, @RequestBody SlaPolicyRequest request) {
        return slaPolicyService.updatePolicy(id, request);
    }

    @DeleteMapping("/{id}")
    public void deletePolicy(@PathVariable Long id) {
        slaPolicyService.deletePolicy(id);
    }

    @GetMapping("/breach-percentage")
    public double calculateSlaBreachPercentage(
            @RequestParam Long slaPolicyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ticketCreationTime
    ) {
        return slaPolicyService.calculateSlaBreachPercentage(slaPolicyId, ticketCreationTime);
    }

    @GetMapping("/ticket/{ticketId}/policy")
    public SlaPolicy getSlaPolicyForTicket(@PathVariable Long ticketId) {
        return slaPolicyService.getSlaPolicyForTicket(ticketId);
    }

}

