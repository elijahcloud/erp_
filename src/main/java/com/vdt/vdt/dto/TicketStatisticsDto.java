package com.vdt.vdt.dto;

import lombok.Data;

@Data
public class TicketStatisticsDto {

    private int totalTickets;
    private double slaCompliance;
    private double avgResponseTime;
    private double avgResolutionTime;


    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public double getSlaCompliance() {
        return slaCompliance;
    }

    public void setSlaCompliance(double slaCompliance) {
        this.slaCompliance = slaCompliance;
    }

    public double getAvgResponseTime() {
        return avgResponseTime;
    }

    public void setAvgResponseTime(double avgResponseTime) {
        this.avgResponseTime = avgResponseTime;
    }

    public double getAvgResolutionTime() {
        return avgResolutionTime;
    }

    public void setAvgResolutionTime(double avgResolutionTime) {
        this.avgResolutionTime = avgResolutionTime;
    }
}

