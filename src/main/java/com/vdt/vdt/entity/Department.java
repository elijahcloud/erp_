package com.vdt.vdt.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long id;

    @Column(name = "department_name", nullable = false)
    private String name;

    @Column(name = "department_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "department_created_by")
    private Long createdBy;

    @Column(name = "department_updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "department_updated_by")
    private Long updatedBy;

    @Column(name = "department_deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "department_deleted_by")
    private Long deletedBy;
}
