package com.vdt.vdt.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "customer_billings")
@Data
public class CustomerBilling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_billing_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "customer_billing_customer_id", referencedColumnName = "customer_id", nullable = false, unique = true)
    @JsonIgnore
    private Customer customer;

    @Column(name = "customer_billing_payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "customer_billing_billing_cycle", nullable = false)
    private String billingCycle;

    @Column(name = "customer_billing_currency", nullable = false)
    private String currency;

    @Column(name = "customer_billing_tax_id", nullable = false)
    private String taxId;

    @Column(name = "customer_billing_created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "customer_billing_created_by", updatable = false)
    private Long createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_billing_created_by", insertable = false, updatable = false)
    private User createdByUser;

    @Column(name = "customer_billing_updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "customer_billing_updated_by")
    private Long updatedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_billing_updated_by", insertable = false, updatable = false)
    private User updatedByUser;

    @Column(name = "customer_billing_deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "customer_billing_deleted_by")
    private Long deletedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_billing_deleted_by", insertable = false, updatable = false)
    private User deletedByUser;
}
