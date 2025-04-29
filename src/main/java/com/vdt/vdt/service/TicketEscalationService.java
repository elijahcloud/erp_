package com.vdt.vdt.service;

import com.vdt.vdt.entity.SlaPolicy;
import com.vdt.vdt.entity.Ticket;
import com.vdt.vdt.entity.TicketPriority;
import com.vdt.vdt.entity.TicketStatus;
import com.vdt.vdt.repository.SlaPolicyRepository;
import com.vdt.vdt.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketEscalationService {

    private final TicketRepository ticketRepository;
    private final SlaPolicyRepository slaRuleRepository;
    private final AsyncNotificationService notificationService;
    private static final int PAGE_SIZE = 100;

    private static final Logger log = LoggerFactory.getLogger(TicketEscalationService.class);

    public TicketEscalationService(TicketRepository ticketRepository, SlaPolicyRepository slaRuleRepository, AsyncNotificationService notificationService) {
        this.ticketRepository = ticketRepository;
        this.slaRuleRepository = slaRuleRepository;
        this.notificationService = notificationService;
    }
    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void checkTicketsForEscalation() {
        int page = 0;
        Page<Ticket> openTicketsPage;

        do {
            openTicketsPage = ticketRepository.findOpenTickets(PageRequest.of(page, PAGE_SIZE));

            for (Ticket ticket : openTicketsPage.getContent()) {
                Optional<SlaPolicy> slaRule = slaRuleRepository.findMatchingPolicy(ticket.getType(), ticket.getPriority(), ticket.getCustomer().getAccountType());
                if (slaRule.isEmpty()) continue;

                boolean updated = false;
                LocalDateTime now = LocalDateTime.now();

                if (slaRule.get().getReassignThresholdHours() != null &&
                        ticket.getFirstResponseAt() == null &&
                        Duration.between(ticket.getCreatedAt(), now).toHours() >= slaRule.get().getReassignThresholdHours()) {
//todo check the reassigned well
//                    ticketService.reassignTicket(ticket.getId(), ticket.getAssignedAgent().getEmail());
                    log.info("Ticket [{}] reassigned due to no response within threshold.", ticket.getId());
                    updated = true;
                }

                if (!ticket.isResponseSlaBreached() && ticket.getSlaResponseDueAt() != null && now.isAfter(ticket.getSlaResponseDueAt())) {
                    ticket.setResponseSlaBreached(true);
                    ticket.setStatus(TicketStatus.ESCALATED);


                    notificationService.notifyManager(ticket.getAssignedAgent(), ticket.getId());
                    notificationService.notifyAgent(ticket.getAssignedAgent(), ticket.getId());

                    log.info("Ticket [{}] response SLA breached. Escalated. Manager and agent notified.", ticket.getId());
                    updated = true;
                }


                if (updated) {
                    ticketRepository.save(ticket);
                }
            }

            page++;
        } while (openTicketsPage.hasNext());
    }

    @Scheduled(fixedRate = 15 * 60 * 1000)
    public void checkTicketsForSlaRisk() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endTime = now.plusMinutes(30);

        List<Ticket> tickets = ticketRepository.findTicketsNearSlaBreach(now, endTime);
        for (Ticket ticket : tickets) {
            notificationService.notifyManager(ticket.getAssignedAgent(), ticket.getId());
            log.info("Ticket [{}] is at risk of SLA breach. Supervisor notified.", ticket.getId());
        }
    }

}
