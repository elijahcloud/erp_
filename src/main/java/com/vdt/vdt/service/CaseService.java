package com.vdt.vdt.service;

import com.vdt.vdt.dto.CaseResponse;
import com.vdt.vdt.dto.CreateCaseRequest;
import com.vdt.vdt.entity.*;
import com.vdt.vdt.repository.CaseEscalationLogRepository;
import com.vdt.vdt.repository.CaseRepository;
import com.vdt.vdt.repository.TicketRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CaseService {

    private final CaseRepository caseRepository;
    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;
    private final CaseEscalationLogRepository caseEscalationLogRepository;

    public CaseService(CaseRepository caseRepository, TicketRepository ticketRepository, ModelMapper modelMapper, CaseEscalationLogRepository caseEscalationLogRepository) {
        this.caseRepository = caseRepository;
        this.ticketRepository = ticketRepository;
        this.modelMapper = modelMapper;
        this.caseEscalationLogRepository = caseEscalationLogRepository;
    }

    public CaseResponse createCase(CreateCaseRequest request) {
        Case newCase = new Case();
        newCase.setCaseTitle(request.getCaseTitle());
        newCase.setCaseType(CaseType.valueOf(request.getCaseType()));
        newCase.setCaseStatus(TicketStatus.OPEN);
        newCase.setCreatedAt(LocalDateTime.now());
        newCase.setUpdatedAt(LocalDateTime.now());

        List<Ticket> tickets = ticketRepository.findAllById(request.getTicketIds());
        for (Ticket ticket : tickets) {
            ticket.setTicketCase(newCase);
        }

        newCase.setTickets(tickets);
        Case savedCase = caseRepository.save(newCase);
        ticketRepository.saveAll(tickets);

        return modelMapper.map(savedCase, CaseResponse.class);
    }

    public CaseResponse getCaseById(Long id) {
        Case found = caseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Case not found"));
        return modelMapper.map(found, CaseResponse.class);
    }

    public void closeCase(Long caseId) {
        Case caseToBeClosed = caseRepository.findById(caseId).orElseThrow();
        caseToBeClosed.setCaseStatus(TicketStatus.CLOSED);
        caseToBeClosed.setResolvedAt(LocalDateTime.now());
        caseRepository.save(caseToBeClosed);
    }

    public double getSlaCompliancePercentageForCase(Long caseId) {
        Case foundCase = caseRepository.findById(caseId).orElseThrow(
                () -> new RuntimeException("case not found")
        );
        List<Ticket> tickets = ticketRepository.findByTicketCase(foundCase);
        if (tickets.isEmpty()) return 0;

        long compliant = tickets.stream()
                .filter(this::isSlaCompliant)
                .count();

        return (double) compliant / tickets.size() * 100;
    }

    private boolean isSlaCompliant(Ticket ticket) {
        return !ticket.isResolutionSlaBreached() && !ticket.isResponseSlaBreached();
    }


    public void tryAutoCloseCase(Long caseId) {
        Case ticketCase = caseRepository.findById(caseId).orElseThrow();
        List<Ticket> tickets = ticketRepository.findByTicketCase(ticketCase);

        boolean allClosed = tickets.stream().allMatch(t -> t.getStatus() == TicketStatus.CLOSED);
        if (allClosed) {
            ticketCase.setCaseStatus(TicketStatus.CLOSED);
            ticketCase.setResolvedAt(LocalDateTime.now());
            caseRepository.save(ticketCase);
        }
    }

    public void escalateCaseAndTicketsById(Long caseId, String reason) {
        Case ticketCase = caseRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException("case not found"));
        escalateCaseAndTickets(ticketCase, reason);
    }
    private void escalateCaseAndTickets(Case ticketCase, String reason) {
        EscalationLevel fromLevel = ticketCase.getEscalationLevel();
        if (fromLevel == null) fromLevel = EscalationLevel.LEVEL_1;
        EscalationLevel toLevel = fromLevel.next();

        if (toLevel != fromLevel) {
            ticketCase.setEscalationLevel(toLevel);
            ticketCase.setLastEscalatedAt(LocalDateTime.now());

            CaseEscalationLog log = new CaseEscalationLog();
            log.setParentCase(ticketCase);
            log.setFromLevel(fromLevel);
            log.setToLevel(toLevel);
            log.setEscalatedAt(LocalDateTime.now());
            log.setEscalatedBy("SYSTEM");
            log.setReason(reason);

            caseEscalationLogRepository.save(log);


            List<Ticket> openTickets = ticketRepository.findByTicketCase(ticketCase).stream()
                    .filter(t -> t.getStatus() != TicketStatus.CLOSED)
                    .toList();


            ticketRepository.saveAll(openTickets);
            caseRepository.save(ticketCase);
        }


    }
}


