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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(String billingCycle) {
        this.billingCycle = billingCycle;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public User getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public User getUpdatedByUser() {
        return updatedByUser;
    }

    public void setUpdatedByUser(User updatedByUser) {
        this.updatedByUser = updatedByUser;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Long getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(Long deletedBy) {
        this.deletedBy = deletedBy;
    }

    public User getDeletedByUser() {
        return deletedByUser;
    }

    public void setDeletedByUser(User deletedByUser) {
        this.deletedByUser = deletedByUser;
    }
}
