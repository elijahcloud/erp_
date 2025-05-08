package com.vdt.vdt.dto;

import lombok.Data;

@Data
public class TicketTypeSlaPerformanceDTO {
    private String ticketType;
    private double compliancePercentage;

    public TicketTypeSlaPerformanceDTO(String ticketType, double compliancePercentage) {
        this.ticketType = ticketType;
        this.compliancePercentage = compliancePercentage;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public double getCompliancePercentage() {
        return compliancePercentage;
    }

    public void setCompliancePercentage(double compliancePercentage) {
        this.compliancePercentage = compliancePercentage;
    }
}

