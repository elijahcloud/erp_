package com.vdt.vdt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cases")
public class Case {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "case_id")
    private Long id;

    private String caseTitle;
    private String description;

    @Enumerated(EnumType.STRING)
    private CaseType caseType;

    @Enumerated(EnumType.STRING)
    private TicketStatus caseStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;
    private LocalDateTime lastEscalatedAt;

    private LocalDateTime slaResolutionDueAt;

    @Enumerated(EnumType.STRING)
    private EscalationLevel escalationLevel;

    @Enumerated(EnumType.STRING)
    private CustomerTier customerTier;

    @Enumerated(EnumType.STRING)
    private IssueType issueType;


    @OneToMany(mappedBy = "ticketCase", fetch = FetchType.LAZY)
    private List<Ticket> tickets;

    public LocalDateTime getSlaResolutionDueAt() {
        return slaResolutionDueAt;
    }

    public void setSlaResolutionDueAt(LocalDateTime slaResolutionDueAt) {
        this.slaResolutionDueAt = slaResolutionDueAt;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(String caseTitle) {
        this.caseTitle = caseTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CaseType getCaseType() {
        return caseType;
    }

    public void setCaseType(CaseType caseType) {
        this.caseType = caseType;
    }

    public TicketStatus getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(TicketStatus caseStatus) {
        this.caseStatus = caseStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public EscalationLevel getEscalationLevel() {
        return escalationLevel;
    }

    public void setEscalationLevel(EscalationLevel escalationLevel) {
        this.escalationLevel = escalationLevel;
    }

    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }

    public LocalDateTime getLastEscalatedAt() {
        return lastEscalatedAt;
    }

    public void setLastEscalatedAt(LocalDateTime lastEscalatedAt) {
        this.lastEscalatedAt = lastEscalatedAt;
    }

    public CustomerTier getCustomerTier() {
        return customerTier;
    }

    public void setCustomerTier(CustomerTier customerTier) {
        this.customerTier = customerTier;
    }
}
