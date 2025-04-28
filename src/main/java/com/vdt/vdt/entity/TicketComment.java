package com.vdt.vdt.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "ticket_comments")
public class TicketComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_comment_id")
    private Long id;

    @Column(name = "ticket_comment_text", nullable = false)
    private String comment;

    @Column(name = "ticket_comment_author")
    private String authorName;

    @Column(name = "ticket_comment_timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_comment_ticket_id", nullable = false)
    private Ticket ticket;

    @Column(name = "ticket_id", nullable = false)
    private Long ticketId;

    @Column(name = "ticket_comment_created_by")
    private Long createdBy;

    @Column(name = "ticket_comment_updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "ticket_comment_updated_by")
    private Long updatedBy;

    @Column(name = "ticket_comment_deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "ticket_comment_deleted_by")
    private Long deletedBy;
}

