package com.vdt.vdt.dto;

import com.vdt.vdt.entity.Ticket;
import lombok.Data;

@Data
public class TicketDashboardDto {
    private long ticketId;
    private String slaType;
    private String status;
    private String responseStatus;
    private String resolutionStatus;
    private String remainingTime;
    private int progress;


    public TicketDashboardDto(Ticket ticket, String slaStatus, String remainingTime) {
        this.ticketId = ticket.getId();
        this.status = slaStatus;
        this.remainingTime = remainingTime;
    }
}
