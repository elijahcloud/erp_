package com.vdt.vdt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sla_policies")
public class SlaPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    @Enumerated(EnumType.STRING)
    private TicketPriority priority;

    private CustomerAccountType customerGroup;

    private Duration responseTimeTarget;
    private Duration resolutionTimeTarget;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer reassignThresholdHours;

    public Integer getReassignThresholdHours() {
        return reassignThresholdHours;
    }

    public void setReassignThresholdHours(Integer reassignThresholdHours) {
        this.reassignThresholdHours = reassignThresholdHours;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public Duration getResolutionTimeTarget() {
        return resolutionTimeTarget;
    }

    public void setResolutionTimeTarget(Duration resolutionTimeTarget) {
        this.resolutionTimeTarget = resolutionTimeTarget;
    }

    public Duration getResponseTimeTarget() {
        return responseTimeTarget;
    }

    public void setResponseTimeTarget(Duration responseTimeTarget) {
        this.responseTimeTarget = responseTimeTarget;
    }

    public CustomerAccountType getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(CustomerAccountType customerGroup) {
        this.customerGroup = customerGroup;
    }

    public TicketPriority getPriority() {
        return priority;
    }

    public void setPriority(TicketPriority priority) {
        this.priority = priority;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

