package com.vdt.vdt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CaseEscalationLog {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Case parentCase;

    private String reason;
    private EscalationLevel fromLevel;
    private EscalationLevel toLevel;

    private String escalatedBy;
    private LocalDateTime escalatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Case getParentCase() {
        return parentCase;
    }

    public void setParentCase(Case parentCase) {
        this.parentCase = parentCase;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public EscalationLevel getFromLevel() {
        return fromLevel;
    }

    public void setFromLevel(EscalationLevel fromLevel) {
        this.fromLevel = fromLevel;
    }

    public EscalationLevel getToLevel() {
        return toLevel;
    }

    public void setToLevel(EscalationLevel toLevel) {
        this.toLevel = toLevel;
    }

    public String getEscalatedBy() {
        return escalatedBy;
    }

    public void setEscalatedBy(String escalatedBy) {
        this.escalatedBy = escalatedBy;
    }

    public LocalDateTime getEscalatedAt() {
        return escalatedAt;
    }

    public void setEscalatedAt(LocalDateTime escalatedAt) {
        this.escalatedAt = escalatedAt;
    }
}

