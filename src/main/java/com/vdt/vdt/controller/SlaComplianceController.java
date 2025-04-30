package com.vdt.vdt.controller;

import com.vdt.vdt.service.SlaComplianceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sla/compliance")

public class SlaComplianceController {

    private final SlaComplianceService slaComplianceService;

    private SlaComplianceController(SlaComplianceService slaComplianceService) {
        this.slaComplianceService = slaComplianceService;
    }

    @GetMapping("/resolved-percentage")
    public double getResolvedWithinSlaPercentage() {
        return slaComplianceService.getResolvedWithinSlaPercentage();
    }

    @GetMapping("/average-response-time")
    public double getAverageResponseTime() {
        return slaComplianceService.getAverageResponseTime();
    }

    @GetMapping("/breaches-per-agent")
    public Map<String, Long> getSlaBreachesPerAgent() {
        return slaComplianceService.getSlaBreachesPerAgent();
    }

    @GetMapping("/performance-by-ticket-type")
    public Map<String, Double> getSlaPerformanceByTicketType() {
        return slaComplianceService.getSlaPerformanceByTicketType();
    }

    @GetMapping("/top-violators")
    public List<String> getTopSlaViolators() {
        return slaComplianceService.getTopSlaViolators();
    }
}

