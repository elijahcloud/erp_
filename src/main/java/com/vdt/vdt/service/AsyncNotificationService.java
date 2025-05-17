package com.vdt.vdt.service;

import com.vdt.vdt.entity.Customer;
import com.vdt.vdt.entity.Ticket;
import com.vdt.vdt.entity.User;
import com.vdt.vdt.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service


public class AsyncNotificationService {

    private final TicketRepository ticketRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    private final CustomerService customerService;

    private static final Logger log = LoggerFactory.getLogger(AsyncNotificationService.class);

    public AsyncNotificationService(TicketRepository ticketRepository, UserService userService, NotificationService notificationService, CustomerService customerService) {
        this.ticketRepository = ticketRepository;
        this.userService = userService;
        this.notificationService = notificationService;
        this.customerService = customerService;
    }
    @Async
    public void notifyManager(User assignedAgent, Long ticketId) {
        try {

            Ticket ticket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> new EntityNotFoundException("Ticket not found with ID: " + ticketId));


            User manager = userService.getManagerOfUserAssignedToATicket(assignedAgent.getId());

            if (manager != null) {
                notificationService.sendNotification(
                        manager.getEmail(),
                        "SLA Breach Alert",
                        "Ticket ID " + ticket.getId() + " has breached SLA and needs your attention, please reassign the ticket."
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
                log.info("Successfully alerted agent [{}] for critical ticket [{}].", agent.get().getEmail(), ticketId);

            } else {
                log.warn("No agent found to alert for critical ticket sla breach [{}]", ticketId);
            }
        } catch (Exception ex) {
            log.error("Error while alerting agent [{}]: {}", ticketId, ex.getMessage(), ex);
        }
    }

    private static void getInfo(Long ticketId, Optional<User> agent) {
        log.info("Successfully alerted CRM Supervisor [{}] for critical ticket [{}].", agent.get().getEmail(), ticketId);
    }

    @Async
    public void notifyAssignedAgent(User assignedAgent, Long ticketId) {
        try {
            Ticket ticket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> new EntityNotFoundException("Ticket not found with ID: " + ticketId));

            Optional<User> agent = userService.findByUser(assignedAgent);

            if (agent.isPresent()) {
                notificationService.sendNotification(
                        agent.get().getEmail(),
                        "Assigned Ticket",
                        "Ticket with id " + ticket.getId() + " has been assigned to you, please respond within " +
                                formatDurationUntil(ticket.getSlaResponseDueAt()) + " minutes."

                );
                getInfo(ticketId, agent);
            } else {
                log.warn("No agent found to alert [{}]", ticketId);

            }
        } catch (Exception ex) {
            log.error("Error while alerting agent [{}]: {}", ticketId, ex.getMessage(), ex);
        }
    }

    public static String formatDurationUntil(LocalDateTime targetTime) {
        if (targetTime == null) {
            return "unknown time";
        }

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(now, targetTime);
        long seconds = duration.getSeconds();

        if (seconds <= 0) {
            return "0 seconds";
        }

        if (seconds < 60) {
            return seconds + " seconds";
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            long remainingSeconds = seconds % 60;
            return minutes + " minutes" + (remainingSeconds > 0 ? " " + remainingSeconds + " seconds" : "");
        } else {
            long hours = seconds / 3600;
            long remainingMinutes = (seconds % 3600) / 60;
            return hours + " hours" + (remainingMinutes > 0 ? " " + remainingMinutes + " minutes" : "");
        }
    }

    @Async
    public void notifyCrmSupervisor(Long ticketId) {
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
                getInfo(ticketId, Optional.of(crmSupervisor));
            } else {
                getWarn(ticketId);
            }
        } catch (Exception ex) {
            log.error("Error while alerting CRM Supervisor for critical ticket [{}]: {}", ticketId, ex.getMessage(), ex);
        }
    }

    private static void getWarn(Long ticketId) {
        log.warn("No CRM Supervisor found to alert for critical ticket [{}]", ticketId);
    }


    @Async
    public void notifyCustomer(Customer customer, Long id) {
        try {
            Ticket ticket = ticketRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Ticket not found with ID: " + id));

            Optional<Customer> optionalCustomer = Optional.ofNullable(customerService.findById(customer.getId()));

            if (optionalCustomer.isPresent()) {
                notificationService.sendNotification(
                        optionalCustomer.get().getEmail(),
                        "Ticket Creation Alert",
                        "Ticket with id " + ticket.getId() + " has been created successfully."
                );
                log.info("Successfully alerted Customer [{}] for ticket creation [{}].", optionalCustomer.get().getEmail(), id);

            } else {
                log.warn("No Customer found to alert for ticket creation [{}]", id);

            }
        } catch (Exception ex) {
            log.error("Error while alerting Customer for ticket creation [{}]: {}", id, ex.getMessage(), ex);
        }
    }
}

