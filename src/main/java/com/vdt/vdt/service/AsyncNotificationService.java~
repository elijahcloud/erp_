package com.vdt.vdt.service;

import com.vdt.vdt.entity.Ticket;
import com.vdt.vdt.entity.User;
import com.vdt.vdt.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service


public class AsyncNotificationService {

    private final TicketRepository ticketRepository;
    private final UserService userService;
    private final NotificationService notificationService;

    private static final Logger log = LoggerFactory.getLogger(AsyncNotificationService.class);

    public AsyncNotificationService(TicketRepository ticketRepository, UserService userService, NotificationService notificationService) {
        this.ticketRepository = ticketRepository;
        this.userService = userService;
        this.notificationService = notificationService;
    }
    @Async
    public void notifyManager(User assignedAgent, Long ticketId) {
        try {

            Ticket ticket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> new EntityNotFoundException("Ticket not found with ID: " + ticketId));


            User manager = userService.getManagerOfUserAssignedToATicket(assignedAgent.getId());  // Using assignedAgent ID here

            if (manager != null) {
                notificationService.sendNotification(
                        manager.getEmail(),
                        "SLA Breach Alert",
                        "Ticket ID " + ticket.getId() + " has breached SLA and needs your attention."
                );
                log.info("Successfully notified Manager [{}] for ticket [{}] SLA breach.", manager.getEmail(), ticketId);
            } else {
                log.warn("No manager found to notify for ticket [{}]", ticketId);
            }
        } catch (Exception ex) {
            log.error("Error while notifying manager for ticket [{}]: {}", ticketId, ex.getMessage(), ex);
        }
    }


    @Async
    public void alertCrmSupervisor(Long ticketId) {
        try {
            Ticket ticket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> new EntityNotFoundException("Ticket not found with ID: " + ticketId));

            User crmSupervisor = userService.getCrmSupervisor();

            if (crmSupervisor != null) {
                notificationService.sendNotification(
                        crmSupervisor.getEmail(),
                        "Critical Ticket SLA Breach",
                        "Critical Ticket ID " + ticket.getId() + " has breached SLA. Immediate action required."
                );
                log.info("Successfully alerted CRM Supervisor [{}] for critical ticket [{}].", crmSupervisor.getEmail(), ticketId);
            } else {
                log.warn("No CRM Supervisor found to alert for critical ticket [{}]", ticketId);
            }
        } catch (Exception ex) {
            log.error("Error while alerting CRM Supervisor for critical ticket [{}]: {}", ticketId, ex.getMessage(), ex);
        }
    }


    @Async
    public void notifyUser(String email, String message, long ticketId) {
        notificationService.sendNotification(
                email,
                "new ticket assigned "+ticketId,
                message
        );
    }

    @Async
    public void notifyAgent(User assignedAgent, Long ticketId) {
        try {
            Ticket ticket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> new EntityNotFoundException("Ticket not found with ID: " + ticketId));

            Optional<User> agent = userService.findByUser(assignedAgent);

            if (agent.isPresent()) {
                notificationService.sendNotification(
                        agent.get().getEmail(),
                        "Critical Ticket SLA Breach",
                        "Critical Ticket ID " + ticket.getId() + " has breached SLA. Immediate action required."
                );
                log.info("Successfully alerted CRM Supervisor [{}] for critical ticket [{}].", agent.get().getEmail(), ticketId);
            } else {
                log.warn("No CRM Supervisor found to alert for critical ticket [{}]", ticketId);
            }
        } catch (Exception ex) {
            log.error("Error while alerting CRM Supervisor for critical ticket [{}]: {}", ticketId, ex.getMessage(), ex);
        }
    }
}

