package com.vdt.vdt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "prices")
@Data
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    private Long id;

    private double amount;

    @Column(name = "created_at", columnDefinition = "DATETIME")
    LocalDateTime createdAt;

    @ToString.Exclude
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "erp_service_id", referencedColumnName = "erp_service_id")
    ERPServiceType erpServiceType;
}
