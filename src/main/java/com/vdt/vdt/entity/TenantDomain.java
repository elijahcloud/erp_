package com.vdt.vdt.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tenant_domains")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TenantDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_domain_id")
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_domain_tenant_id", nullable = false) // Ensure the column name matches the Tenant entity's primary key
    private Tenant tenant;

    @Column(name = "tenant_domain_name", nullable = false, unique = true, length = 255)
    private String name;

    @Column(name = "tenant_domain_is_primary", nullable = false)
    private Boolean isPrimary;

    @Column(name = "tenant_domain_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "tenant_domain_updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "tenant_domain_deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "tenant_domain_deleted_by")
    private User deletedBy;
}
