package com.vdt.vdt.service;

import com.vdt.vdt.entity.Case;
import com.vdt.vdt.entity.TicketStatus;
import com.vdt.vdt.repository.CaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CaseEscalationService {

    private final CaseRepository caseRepository;
    private final CaseSlaEvaluator caseSlaEvaluator;

    public CaseEscalationService (CaseRepository caseRepository, CaseSlaEvaluator caseSlaEvaluator){
        this.caseRepository = caseRepository;
        this.caseSlaEvaluator = caseSlaEvaluator;
    }

    @Scheduled(fixedDelay = 30 * 60 * 1000)
    @Async
    public void checkCasesForEscalation() {
        PageRequest pageRequest = PageRequest.of(0, 100);
        Page<Case> openCases;

        do {
            openCases = caseRepository.findByCaseStatus(TicketStatus.OPEN, pageRequest);;
            for (Case ticketCase : openCases) {
                boolean changed = caseSlaEvaluator.evaluate(ticketCase);
                if (changed) {
                    caseRepository.save(ticketCase);
                }
            }
            pageRequest = pageRequest.next();
        } while (openCases.hasNext());
    }

}
