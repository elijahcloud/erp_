package com.vdt.vdt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CustomerAccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(CustomerAccountType accountType) {
        this.accountType = accountType;
    }

    public CustomerTier getCustomerTier() {
        return customerTier;
    }

    public void setCustomerTier(CustomerTier customerTier) {
        this.customerTier = customerTier;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public User getAgent() {
        return agent;
    }

    public void setAgent(User agent) {
        this.agent = agent;
    }

    public List<CustomerNote> getNotes() {
        return notes;
    }

    public void setNotes(List<CustomerNote> notes) {
        this.notes = notes;
    }

    public CustomerBilling getBilling() {
        return billing;
    }

    public void setBilling(CustomerBilling billing) {
        this.billing = billing;
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
