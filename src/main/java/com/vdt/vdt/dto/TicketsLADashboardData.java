package com.vdt.vdt.dto;

import com.vdt.vdt.entity.Ticket;
import lombok.Data;

import java.util.List;

@Data
public class TicketsLADashboardData {


        private long ticketId;
        private String slaType;
        private String status;
        private String responseStatus;
        private String resolutionStatus;
        private String remainingTime;
        private int progress;
        private long ticketSize;


    public TicketsLADashboardData(int size, int onTrack, int atRisk, int breached, List<TicketDashboardDto> ticketDetails) {
        this.ticketSize = size;
        this.progress = onTrack;

    }
}
