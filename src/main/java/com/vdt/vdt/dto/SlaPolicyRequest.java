package com.vdt.vdt.dto;


import lombok.Data;

@Data
public class SlaPolicyRequest {

    private String ticketType;
    private String priority;
    private String customerGroup;
    private long responseTimeTargetInMinutes;
    private long resolutionTimeTargetInMinutes;
    private Integer reassignThresholdHours;

    public Integer getReassignThresholdHours() {
        return reassignThresholdHours;
    }

    public void setReassignThresholdHours(Integer reassignThresholdHours) {
        this.reassignThresholdHours = reassignThresholdHours;
    }

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

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }



    public String getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(String customerGroup) {
        this.customerGroup = customerGroup;
    }




}
