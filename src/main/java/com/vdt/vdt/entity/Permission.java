package com.vdt.vdt.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Long id;

    @Column(name = "permission_code", nullable = false, unique = true, length = 100)
    private String code;

    @Column(name = "permission_name", nullable = false, length = 255)
    private String name;

    @Column(name = "permission_module", nullable = false, length = 100)
    private String module;

    @Column(name = "permission_description", length = 500)
    private String description;

    @Column(name = "permission_created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_created_by")
    private User createdBy;

    @Column(name = "permission_updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_updated_by")
    private User updatedBy;
}
