package com.vdt.vdt.entity;

import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_customer_id", nullable = false)
    private Customer customer;

    @Column(name = "ticket_title", nullable = false)
    private String ticketTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_type")
    private TicketType type;

    @Column(name = "ticket_description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_priority")
    private TicketPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_status")
    private TicketStatus status;

    @Column(name = "ticket_source")
    private String source;

    @ManyToOne
    @JoinColumn(name = "ticket_assigned_department_id")
    private Department assignedDepartment;

    public long getResolutionTimeMinutes() {
        return resolutionTimeMinutes;
    }

    public void setResolutionTimeMinutes(long resolutionTimeMinutes) {
        this.resolutionTimeMinutes = resolutionTimeMinutes;
    }

    public long getResponseTimeMinutes() {
        return responseTimeMinutes;
    }

    public void setResponseTimeMinutes(long responseTimeMinutes) {
        this.responseTimeMinutes = responseTimeMinutes;
    }

    @OneToOne
    @JoinColumn(name = "ticket_assigned_agent")
    private User assignedAgent;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketComment> comments = new ArrayList<>();

    @Column(name = "ticket_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "ticket_created_by")
    private Long createdBy;

    @Column(name = "ticket_updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "ticket_updated_by")
    private Long updatedBy;

    @Column(name = "ticket_deleted", nullable = false)
    private boolean deleted;

    @Column(name = "ticket_deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "ticket_deleted_by")
    private Long deletedBy;

    @Column
    private LocalDateTime lastActionAt;

    @Column(name = "first_response_at")
    private LocalDateTime firstResponseAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "sla_response_due_at")
    private LocalDateTime slaResponseDueAt;

    @Column(name = "sla_resolution_due_at")
    private LocalDateTime slaResolutionDueAt;

    @Column(name = "is_response_sla_breached")
    private boolean responseSlaBreached = false;

    @Column(name = "is_resolution_sla_breached")
    private boolean resolutionSlaBreached = false;

    private boolean slaTimerPaused = false;
    private String pauseReason;
    private LocalDateTime slaPausedAt;

    private long responseTimeMinutes;
    private long resolutionTimeMinutes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getTicketTitle() {
        return ticketTitle;
    }

    public void setTicketTitle(String ticketTitle) {
        this.ticketTitle = ticketTitle;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketPriority getPriority() {
        return priority;
    }

    public void setPriority(TicketPriority priority) {
        this.priority = priority;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Department getAssignedDepartment() {
        return assignedDepartment;
    }

    public void setAssignedDepartment(Department assignedDepartment) {
        this.assignedDepartment = assignedDepartment;
    }

    public User getAssignedAgent() {
        return assignedAgent;
    }

    public void setAssignedAgent(User assignedAgent) {
        this.assignedAgent = assignedAgent;
    }

    public List<TicketComment> getComments() {
        return comments;
    }

    public void setComments(List<TicketComment> comments) {
        this.comments = comments;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Long getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(Long deletedBy) {
        this.deletedBy = deletedBy;
    }

    public LocalDateTime getLastActionAt() {
        return lastActionAt;
    }

    public void setLastActionAt(LocalDateTime lastActionAt) {
        this.lastActionAt = lastActionAt;
    }

    public LocalDateTime getFirstResponseAt() {
        return firstResponseAt;
    }

    public void setFirstResponseAt(LocalDateTime firstResponseAt) {
        this.firstResponseAt = firstResponseAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public LocalDateTime getSlaResponseDueAt() {
        return slaResponseDueAt;
    }

    public void setSlaResponseDueAt(LocalDateTime slaResponseDueAt) {
        this.slaResponseDueAt = slaResponseDueAt;
    }

    public LocalDateTime getSlaResolutionDueAt() {
        return slaResolutionDueAt;
    }

    public void setSlaResolutionDueAt(LocalDateTime slaResolutionDueAt) {
        this.slaResolutionDueAt = slaResolutionDueAt;
    }

    public boolean isResponseSlaBreached() {
        return responseSlaBreached;
    }

    public void setResponseSlaBreached(boolean responseSlaBreached) {
        this.responseSlaBreached = responseSlaBreached;
    }

    public boolean isResolutionSlaBreached() {
        return resolutionSlaBreached;
    }

    public void setResolutionSlaBreached(boolean resolutionSlaBreached) {
        this.resolutionSlaBreached = resolutionSlaBreached;
    }

    public boolean isSlaTimerPaused() {
        return slaTimerPaused;
    }

    public void setSlaTimerPaused(boolean slaTimerPaused) {
        this.slaTimerPaused = slaTimerPaused;
    }

    public String getPauseReason() {
        return pauseReason;
    }

    public void setPauseReason(String pauseReason) {
        this.pauseReason = pauseReason;
    }

    public LocalDateTime getSlaPausedAt() {
        return slaPausedAt;
    }

    public void setSlaPausedAt(LocalDateTime slaPausedAt) {
        this.slaPausedAt = slaPausedAt;
    }
}

