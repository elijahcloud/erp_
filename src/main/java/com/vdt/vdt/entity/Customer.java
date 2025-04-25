package com.vdt.vdt.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "customers")
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    String name;
    String phoneNumber;
    String email;
    String accountType;
    String customerTier;
    String accountNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id_agent", referencedColumnName = "user_id")
    User agent;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private CustomerBilling billing;


}
