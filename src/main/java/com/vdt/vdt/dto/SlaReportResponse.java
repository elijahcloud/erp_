package com.vdt.vdt.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SlaReportResponse {
    private long totalTickets;
    private long ticketsWithinSla;
    private long ticketsBreached;
    private double averageResponseTimeMinutes;
}
