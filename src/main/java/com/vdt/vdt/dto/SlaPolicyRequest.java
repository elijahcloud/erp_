package com.vdt.vdt.dto;


import lombok.Data;

@Data
public class SlaPolicyRequest {

    private String ticketType;
    private String priority;
    private String customerAccountType;
    private long responseTimeTargetInMinutes;
    private long resolutionTimeTargetInMinutes;
    private Integer reassignThresholdHours;
    private String customerTier;
    private String caseType;



    public String getCustomerTier() {
        return customerTier;
    }

    public void setCustomerTier(String customerTier) {
        this.customerTier = customerTier;
    }

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



    public String getCustomerAccountType() {
        return customerAccountType;
    }

    public void setCustomerAccountType(String customerAccountType) {
        this.customerAccountType = customerAccountType;
    }




}
