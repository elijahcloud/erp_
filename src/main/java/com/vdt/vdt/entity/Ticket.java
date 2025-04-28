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

    @Column(name = "ticket_assigned_agent")
    private String assignedAgent;

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
}

