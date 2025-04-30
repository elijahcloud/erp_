package com.vdt.vdt.dto;

import com.vdt.vdt.entity.TicketPriority;
import com.vdt.vdt.entity.TicketType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SlaPolicyResponse {

    private Long id;
    private TicketType ticketType;
    private TicketPriority priority;
    private String customerGroup;
    private long responseTimeTargetInMinutes;
    private long resolutionTimeTargetInMinutes;
    private Integer reassignThresholdHours;

    private LocalDateTime createdAt;

    public long getResponseTimeTargetInMinutes() {
        return responseTimeTargetInMinutes;
    }

    public void setResponseTimeTargetInMinutes(long responseTimeTargetInMinutes) {
        this.responseTimeTargetInMinutes = responseTimeTargetInMinutes;
    }

    public long getResolutionTimeTargetInMinutes() {
        return resolutionTimeTargetInMinutes;
    }

    public void setResolutionTimeTargetInMinutes(long resolutionTimeTargetInMinutes) {
        this.resolutionTimeTargetInMinutes = resolutionTimeTargetInMinutes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public TicketPriority getPriority() {
        return priority;
    }

    public void setPriority(TicketPriority priority) {
        this.priority = priority;
    }

    public String getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(String customerGroup) {
        this.customerGroup = customerGroup;
    }



    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getReassignThresholdHours() {
        return reassignThresholdHours;
    }

    public void setReassignThresholdHours(Integer reassignThresholdHours) {
        this.reassignThresholdHours = reassignThresholdHours;
    }
}

