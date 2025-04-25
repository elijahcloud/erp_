package com.vdt.vdt.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "customer_billing")
@Data
public class CustomerBilling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    private Customer customer;

    private String paymentMethod;
    private String billingCycle;
    private String currency;
    private String taxId;

}
