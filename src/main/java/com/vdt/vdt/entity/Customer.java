package com.vdt.vdt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vdt.vdt.customercelebration.CustomerCelebration;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "customers")
@Data
@EqualsAndHashCode(exclude = {"customerKYC"})
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(name = "customer_name", nullable = false)
    private String name;

    @Column(name = "customer_phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "customer_email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_account_type", nullable = false)
    private CustomerAccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_tier", nullable = false)
    private CustomerTier customerTier;

    @Column(name = "customer_account_number", nullable = false, unique = true)
    private String accountNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_user_id_agent", referencedColumnName = "user_id")
    private User agent;

    @JsonIgnore
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerNote> notes;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private CustomerBilling billing;

    @Column(name = "customer_created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "customer_created_by", updatable = false)
    private Long createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_created_by", insertable = false, updatable = false)
    private User createdByUser;

    @Column(name = "customer_updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "customer_updated_by")
    private Long updatedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_updated_by", insertable = false, updatable = false)
    private User updatedByUser;

    @Column(name = "customer_deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "customer_deleted_by")
    private Long deletedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_deleted_by", insertable = false, updatable = false)
    private User deletedByUser;

    @OneToOne(mappedBy = "customer")
    private KYC customerKYC;

    @OneToMany(mappedBy = "customer")
    private Set<SubscriptionService> subscriptionServices;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "customer_tenant_id", referencedColumnName = "tenant_id")
    private Tenant tenant;

    @OneToMany(mappedBy = "customer")
    List<CustomerCelebration> customerCelebration;
}
