package com.vdt.vdt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "subscription_services")
@Data
public class SubscriptionService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    BillingCycle billingCycle;

    @ToString.Exclude
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "tenant_id_ref", referencedColumnName = "tenant_id")
    Tenant tenant;

    @ToString.Exclude
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "customer_id_ref", referencedColumnName = "customer_id")
    Customer customer;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "erp_service_type_id_ref", referencedColumnName = "erp_service_id")
    private ERPServiceType erpServiceType;
}
