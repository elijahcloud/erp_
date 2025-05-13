package com.vdt.vdt.service;

import com.vdt.vdt.entity.*;
import com.vdt.vdt.repository.CaseEscalationLogRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CaseSlaEvaluator {
    private final SlaPolicyService slaPolicyService;
    private final CaseEscalationLogRepository caseEscalationLogRepository;

    public CaseSlaEvaluator(SlaPolicyService slaPolicyService, CaseEscalationLogRepository escalationLogRepository) {
        this.slaPolicyService = slaPolicyService;
        this.caseEscalationLogRepository = escalationLogRepository;
    }

    public boolean evaluate(Case ticketCase) {

        CustomerTier customerTier = ticketCase.getCustomerTier();

        SlaPolicy policy = slaPolicyService.getPolicyForCaseTypeAndCustomerTier(ticketCase.getCaseType(), customerTier);

        if (policy == null) return false;

        if (ticketCase.getCreatedAt().plusMinutes(policy.getResolutionTimeTargetInMinutes())
                .isBefore(LocalDateTime.now())) {
            escalate(ticketCase);
            return true;
        }

        return false;
    }

    private void escalate(Case ticketCase) {
        EscalationLevel fromLevel = ticketCase.getEscalationLevel();
        EscalationLevel toLevel = switch (fromLevel) {
            case LEVEL_1 -> EscalationLevel.LEVEL_2;
            case LEVEL_2 -> EscalationLevel.LEVEL_3;
            default -> fromLevel;
        };

        if (toLevel != fromLevel) {
            ticketCase.setEscalationLevel(toLevel);
            ticketCase.setLastEscalatedAt(LocalDateTime.now());

            CaseEscalationLog log = new CaseEscalationLog();
            log.setParentCase(ticketCase);
            log.setFromLevel(fromLevel);
            log.setToLevel(toLevel);
            log.setReason("SLA resolution time exceeded");
            log.setEscalatedAt(LocalDateTime.now());
            log.setEscalatedBy("SYSTEM");

            caseEscalationLogRepository.save(log);
        }
    }
}
