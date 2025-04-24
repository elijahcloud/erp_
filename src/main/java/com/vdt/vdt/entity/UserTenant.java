package com.vdt.vdt.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_tenants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_tenant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_tenant_user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_tenant_tenant_id", nullable = false) // Ensure the column name matches the Tenant entity's primary key
    private Tenant tenant;

    @Column(name = "user_tenant_is_primary", nullable = false)
    private Boolean isPrimary;

    @Column(name = "user_tenant_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "user_tenant_updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "user_tenant_deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_tenant_deleted_by")
    private User deletedBy;
}
