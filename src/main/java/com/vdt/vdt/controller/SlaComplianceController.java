package com.vdt.vdt.controller;

import com.vdt.vdt.dto.AgentSlaBreachDTO;
import com.vdt.vdt.dto.DepartmentSlaBreachDTO;
import com.vdt.vdt.dto.TicketTypeSlaPerformanceDTO;
import com.vdt.vdt.dto.TopSlaViolatorDTO;
import com.vdt.vdt.service.SlaComplianceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sla/compliance")
public class SlaComplianceController {

    private final SlaComplianceService slaComplianceService;

    private SlaComplianceController(SlaComplianceService slaComplianceService) {
        this.slaComplianceService = slaComplianceService;
    }

    @GetMapping("/resolved-percentage")
    public double getResolvedWithinSlaPercentage() {
        try {
            return slaComplianceService.getResolvedWithinSlaPercentage();
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate resolved SLA percentage", e);
        }
    }

    @GetMapping("/average-response-time")
    public double getAverageResponseTime() {
        try {
            return slaComplianceService.getAverageResponseTime();
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate average response time", e);
        }
    }

    @GetMapping("/breaches-per-agent")
    public List<AgentSlaBreachDTO> getSlaBreachesPerAgent() {
        try {
            return slaComplianceService.getSlaBreachesPerAgent();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve SLA breaches per agent", e);
        }
    }

    @GetMapping("/breaches-per-department")
    public List<DepartmentSlaBreachDTO> getSlaBreachesPerDepartment() {
        try {
            return slaComplianceService.getSlaBreachesPerDepartment();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve SLA breaches per department", e);
        }
    }

    @GetMapping("/performance-by-ticket-type")
    public List<TicketTypeSlaPerformanceDTO> getSlaPerformanceByTicketType() {
        try {
            return slaComplianceService.getSlaPerformanceByTicketType();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve SLA performance by ticket type", e);
        }
    }

    @GetMapping("/top-violators")
    public Page<TopSlaViolatorDTO> getTopSlaViolators(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return slaComplianceService.getTopSlaViolators(pageable);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve top SLA violators", e);
        }
    }
}
