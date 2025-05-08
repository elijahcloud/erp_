package com.vdt.vdt.service;

import com.vdt.vdt.entity.Ticket;
import com.vdt.vdt.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TicketEscalationService {

    private final TicketRepository ticketRepository;
    private final TicketSlaEvaluator slaEvaluator;
    private final ExecutorService executorService;
    private static final int PAGE_SIZE = 100;
    private static final Logger log = LoggerFactory.getLogger(TicketEscalationService.class);

    public TicketEscalationService(TicketRepository ticketRepository, TicketSlaEvaluator slaEvaluator, ExecutorService executorService) {
        this.ticketRepository = ticketRepository;
        this.slaEvaluator = slaEvaluator;

        this.executorService = executorService;
    }

    @Async
    @Scheduled(fixedDelay = 30 * 60 * 1000)
    public void checkTicketsForEscalation() {
        long start = System.currentTimeMillis();
        int page = 0;
        Page<Ticket> pageTickets;
        List<Future<?>> futures = new ArrayList<>();

        try {
            do {
                PageRequest pageRequest = PageRequest.of(page++, PAGE_SIZE);
                pageTickets = ticketRepository.findOpenTickets(pageRequest);
                List<Ticket> tickets = pageTickets.getContent();


                futures.add(executorService.submit(() -> {
                    List<Ticket> changedTickets = new ArrayList<>();

                    for (Ticket ticket : tickets) {
                        boolean changed = slaEvaluator.evaluate(ticket);
                        if (changed) {
                            changedTickets.add(ticket);
                        }
                    }

                    if (!changedTickets.isEmpty()) {
                        ticketRepository.saveAll(changedTickets); // batch save
                    }
                }));
            } while (pageTickets.hasNext());


            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    log.error("Error during ticket processing task", e);
                }
            }
        } finally {
            long duration = System.currentTimeMillis() - start;
            log.info("checkTicketsForEscalation completed in {} ms", duration);
        }
    }


    @Scheduled(fixedDelay = 5 * 60 * 1000)
    public void checkTicketsNearSla() {
        long start = System.currentTimeMillis();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nearWindow = now.plusMinutes(60);

        try {
            List<Ticket> nearSlaTickets = ticketRepository.findTicketsNearSlaBreach(now, nearWindow);

            if (!nearSlaTickets.isEmpty()) {
                int chunkSize = 100;

                IntStream.iterate(0, startIndex -> startIndex < nearSlaTickets.size(), startIndex -> startIndex + chunkSize)
                        .mapToObj(startIndex -> nearSlaTickets.subList(startIndex, Math.min(startIndex + chunkSize, nearSlaTickets.size())))
                        .forEach(chunk -> executorService.execute(() -> {
                            try {
                                List<Ticket> changedTickets = chunk.stream()
                                        .filter(slaEvaluator::evaluate)
                                        .collect(Collectors.toList());

                                if (!changedTickets.isEmpty()) {
                                    ticketRepository.saveAll(changedTickets);
                                }
                            } catch (Exception e) {
                                log.error("Error in async ticket processing", e);
                            }
                        }));
            }
        } finally {
            long duration = System.currentTimeMillis() - start;
            log.info("checkTicketsNearSla completed in {} ms", duration);
        }
    }


}

