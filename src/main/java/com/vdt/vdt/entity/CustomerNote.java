package com.vdt.vdt.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

@Entity
@Table(name = "customer_notes")
@Data
public class CustomerNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_note_id")
    private Long id;

    @Column(name = "customer_note_content", columnDefinition = "TEXT", nullable = false)
    private String noteContent;

    @Column(name = "customer_note_pinned", nullable = false)
    private boolean pinned;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_note_customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_note_created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_note_updated_by")
    private User updatedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_note_deleted_by")
    private User deletedBy;

    @Column(name = "customer_note_created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "customer_note_updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "customer_note_deleted_at")
    private LocalDateTime deletedAt;
}
