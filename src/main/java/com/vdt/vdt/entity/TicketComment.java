package com.vdt.vdt.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "ticket_comments")
public class TicketComment {
    @Id
    @GeneratedValue
    private Long id;

    private String comment;
    private String authorName;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;
}

