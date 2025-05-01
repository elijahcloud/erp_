package com.vdt.vdt.service;

import com.vdt.vdt.entity.Ticket;
import com.vdt.vdt.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketEscalationService {

    private final TicketRepository ticketRepository;
    private final TicketSlaEvaluator slaEvaluator;
    private static final int PAGE_SIZE = 100;

    public TicketEscalationService(TicketRepository ticketRepository, TicketSlaEvaluator slaEvaluator) {
        this.ticketRepository = ticketRepository;
        this.slaEvaluator = slaEvaluator;

    }
    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void checkTicketsForEscalation() {
        int page = 0;
        Page<Ticket> pageTickets;

        do {
            pageTickets = ticketRepository.findOpenTickets(PageRequest.of(page++, PAGE_SIZE));

            for (Ticket ticket : pageTickets.getContent()) {
                boolean changed = slaEvaluator.evaluate(ticket);
                if (changed) ticketRepository.save(ticket);
            }

        } while (pageTickets.hasNext());
    }


    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void checkTicketsNearSla() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nearWindow = now.plusMinutes(60);

        List<Ticket> nearSlaTickets = ticketRepository.findTicketsNearSlaBreach(now, nearWindow);

        for (Ticket ticket : nearSlaTickets) {
            boolean changed = slaEvaluator.evaluate(ticket);
            if (changed) ticketRepository.save(ticket);
        }
    }
}

