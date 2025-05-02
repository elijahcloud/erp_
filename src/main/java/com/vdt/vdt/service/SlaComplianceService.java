package com.vdt.vdt.service;

import com.vdt.vdt.entity.CustomerAccountType;
import com.vdt.vdt.entity.SlaPolicy;
import com.vdt.vdt.entity.Ticket;
import com.vdt.vdt.entity.TicketType;
import com.vdt.vdt.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service

public class SlaComplianceService {

    private final TicketRepository ticketRepository;

    public SlaComplianceService (TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public double getResolvedWithinSlaPercentage() {
        long totalTickets = ticketRepository.countTotalTickets();
        long resolvedWithinSla = ticketRepository.countResolvedWithinSla();
        return totalTickets == 0 ? 0 : (double) resolvedWithinSla / totalTickets * 100;
    }
    public double getAverageResponseTime() {
        List<Ticket> ticketsWithResponseTime = ticketRepository.findTicketsWithResponseTime();

        long totalResponseTimeInSeconds = 0;
        long totalTicketsWithResponseTime = ticketsWithResponseTime.size();

        for (Ticket ticket : ticketsWithResponseTime) {
            if (ticket.getCreatedAt() != null && ticket.getFirstResponseAt() != null) {
                totalResponseTimeInSeconds += Duration.between(ticket.getCreatedAt(), ticket.getFirstResponseAt()).getSeconds();
            }
        }

        if (totalTicketsWithResponseTime == 0) {
            return 0;
        }


        return (double) totalResponseTimeInSeconds / totalTicketsWithResponseTime / 60;
    }


    public Map<String, Long> getSlaBreachesPerAgent() {
        List<Object[]> result = ticketRepository.countSlaBreachesPerAgent();
        Map<String, Long> breachesPerAgent = new HashMap<>();
        for (Object[] row : result) {
            breachesPerAgent.put((String) row[0], (Long) row[1]);
        }
        return breachesPerAgent;
    }

    public Map<String, Long> getSlaBreachesPerDepartment() {
        List<Ticket[]> result = ticketRepository.countSlaBreachesPerDepartment();
        Map<String, Long> breachesPerDepartment = new HashMap<>();
        for (Object[] row : result) {
            String departmentName = (String) row[0];
            Long breachCount = (Long) row[1];
            breachesPerDepartment.put(departmentName, breachCount);
        }
        return breachesPerDepartment;
    }


    public Map<String, Double> getSlaPerformanceByTicketType() {
        List<Object[]> result = ticketRepository.countSlaComplianceByTicketType();
        Map<String, Double> slaPerformance = new HashMap<>();
        for (Object[] row : result) {
            String ticketType = (String) row[0];
            long totalTickets = (long) row[1];
            long compliantTickets = (long) row[2];
            slaPerformance.put(ticketType, (double) compliantTickets / totalTickets * 100);
        }
        return slaPerformance;
    }

    public List<String> getTopSlaViolators() {
        List<Object[]> result = ticketRepository.findTopSlaViolators();
        List<String> topViolators = new ArrayList<>();
        for (Object[] row : result) {
            topViolators.add((String) row[0]);
        }
        return topViolators;
    }


//    public SlaPolicy getSlaForCustomerGroupAndTicketType(CustomerAccountType group, TicketType type) {
//        return slaPolicyRepository.findByCustomerAccountTypeAndTicketType(group, type)
//                .orElseThrow(() -> new IllegalArgumentException("SLA configuration not found for the given group and ticket type"));
//    }
//
//    public SlaConfiguration createOrUpdateSlaConfiguration(SlaConfiguration slaConfiguration) {
//        return slaConfigurationRepository.save(slaConfiguration);
//    }
}
