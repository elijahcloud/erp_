package com.vdt.vdt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "tenants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_id")
    private Long id;

    @Basic
    @Column(name = "tenant_name", nullable = false, length = 255)
    private String name;

    @Basic
    @Column(name = "tenant_code", nullable = false, unique = true, length = 100)
    private String code;

    @Basic
    @Column(name = "tenant_is_landlord", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isLandlord;

    @Basic
    @Column(name = "tenant_is_active", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isActive;

    @Basic
    @Column(name = "tenant_created_at", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_created_by", columnDefinition = "BIGINT")
    @JsonIgnore
    private User createdBy;

    @Basic
    @Column(name = "tenant_updated_at", columnDefinition = "DATETIME")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_updated_by", columnDefinition = "BIGINT")
    @JsonIgnore
    private User updatedBy;

    @Basic
    @Column(name = "tenant_deleted_at", columnDefinition = "DATETIME")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_deleted_by", columnDefinition = "BIGINT")
    @JsonIgnore
    private User deletedBy;

    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserTenant> userTenants;

    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<TenantDomain> tenantDomains;

    @PrePersist
    public void prePersist() {
        if (isActive == null) this.isActive = true;
        if (isLandlord == null) this.isLandlord = false;
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
        this.isActive = false;
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    public void setAsLandlord() {
        this.isLandlord = true;
        this.isActive = true;
        this.createdAt = LocalDateTime.now(); // Ensure createdAt is set
    }

    public void associateUser(User user, boolean isPrimary) {
        if (this.userTenants == null) {
            this.userTenants = new ArrayList<>();
        }
        UserTenant userTenant = new UserTenant();
        userTenant.setUser(user);
        userTenant.setTenant(this);
        userTenant.setIsPrimary(isPrimary); // Set primary flag
        userTenant.setCreatedAt(LocalDateTime.now()); // Set createdAt timestamp
        this.userTenants.add(userTenant);
    }
}
