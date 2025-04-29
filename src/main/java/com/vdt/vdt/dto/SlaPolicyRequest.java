package com.vdt.vdt.dto;

import com.vdt.vdt.entity.TicketPriority;
import com.vdt.vdt.entity.TicketType;
import lombok.Data;

@Data
public class SlaPolicyRequest {

    private TicketType ticketType;
    private TicketPriority priority;
    private String customerGroup;
    private Long responseTimeTargetMinutes;
    private Long resolutionTimeTargetMinutes;


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

    public Long getResponseTimeTargetMinutes() {
        return responseTimeTargetMinutes;
    }

    public void setResponseTimeTargetMinutes(Long responseTimeTargetMinutes) {
        this.responseTimeTargetMinutes = responseTimeTargetMinutes;
    }

    public Long getResolutionTimeTargetMinutes() {
        return resolutionTimeTargetMinutes;
    }


}
