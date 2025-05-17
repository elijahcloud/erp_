package com.vdt.vdt.controller;

import com.vdt.vdt.dto.CaseResponse;
import com.vdt.vdt.dto.CreateCaseRequest;
import com.vdt.vdt.dto.SlaComplianceResponse;
import com.vdt.vdt.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    @Autowired
    private CaseService caseService;

    @PostMapping
    public ResponseEntity<CaseResponse> createCase(@RequestBody CreateCaseRequest request) {
        return ResponseEntity.ok(caseService.createCase(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CaseResponse> getCase(@PathVariable Long id) {
        return ResponseEntity.ok(caseService.getCaseById(id));
    }


    @PostMapping("/{id}/close")
    public ResponseEntity<String> close(@PathVariable Long id) {
        caseService.closeCase(id);
        return ResponseEntity.ok("Case closed");
    }

    @GetMapping("/{caseId}/sla-compliance")
    public ResponseEntity<?> getSlaCompliancePercentage(@PathVariable Long caseId) {
        try {
            double percentage = caseService.getSlaCompliancePercentageForCase(caseId);
            SlaComplianceResponse response = new SlaComplianceResponse(caseId, percentage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{caseId}/auto-close")
    public ResponseEntity<?> tryAutoCloseCase(@PathVariable Long caseId) {
        try {
            caseService.tryAutoCloseCase(caseId);
            return ResponseEntity.ok("Case checked for auto-close.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{caseId}/escalate")
    public ResponseEntity<?> escalateCase(@PathVariable Long caseId, @RequestParam String reason) {
        try {
            caseService.escalateCaseAndTicketsById(caseId, reason);
            return ResponseEntity.ok("Case escalated to next level.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
