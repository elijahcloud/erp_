package com.vdt.vdt.service;

import com.vdt.vdt.dto.AgentSlaBreachDTO;
import com.vdt.vdt.dto.DepartmentSlaBreachDTO;
import com.vdt.vdt.dto.TicketTypeSlaPerformanceDTO;
import com.vdt.vdt.dto.TopSlaViolatorDTO;
import com.vdt.vdt.entity.Ticket;
import com.vdt.vdt.entity.TicketType;
import com.vdt.vdt.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service

public class SlaComplianceService {

    private final TicketRepository ticketRepository;
    private static final Logger log = LoggerFactory.getLogger(SlaComplianceService.class);

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


    public List<AgentSlaBreachDTO> getSlaBreachesPerAgent() {
        List<Object[]> result = ticketRepository.countSlaBreachesPerAgent();
        List<AgentSlaBreachDTO> breaches = new ArrayList<>();
        for (Object[] row : result) {
            String agent = String.valueOf(row[0]);
            Long count = (Long) row[1];
            breaches.add(new AgentSlaBreachDTO(agent, count));
        }
        return breaches;
    }


    public List<DepartmentSlaBreachDTO> getSlaBreachesPerDepartment() {
        List<Ticket[]> result = ticketRepository.countSlaBreachesPerDepartment();
        List<DepartmentSlaBreachDTO> breaches = new ArrayList<>();
        for (Object[] row : result) {
            String departmentName = String.valueOf(row[0]);
            Long breachCount = (Long) row[1];
            breaches.add(new DepartmentSlaBreachDTO(departmentName, breachCount));
        }
        return breaches;
    }



    public List<TicketTypeSlaPerformanceDTO> getSlaPerformanceByTicketType() {
        List<Object[]> result = ticketRepository.countSlaComplianceByTicketType();
        List<TicketTypeSlaPerformanceDTO> performanceList = new ArrayList<>();

        for (Object[] row : result) {
            TicketType ticketType = (TicketType) row[0];
            long totalTickets = (long) row[1];
            long compliantTickets = (long) row[2];
            double percentage = totalTickets == 0 ? 0.0 : ((double) compliantTickets / totalTickets) * 100;

            performanceList.add(new TicketTypeSlaPerformanceDTO(ticketType.name(), percentage));
        }

        return performanceList;
    }

    public Page<TopSlaViolatorDTO> getTopSlaViolators(Pageable pageable) {
        Page<Object[]> result = ticketRepository.findTopSlaViolators(pageable);
        return result.map(row -> new TopSlaViolatorDTO(
                (String) row[0],
                ((Number) row[1]).longValue()
        ));
    }


}
