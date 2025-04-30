package com.vdt.vdt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "kyc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KYC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private KYCPackageType kycPackage;

    @Enumerated(EnumType.STRING)
    private KYCStatus kycStatus;

    private LocalDateTime lastUpdated;

    @JsonIgnore
    @Lob
    private byte[] document;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "kyc_customer_id",referencedColumnName = "customer_id", nullable = false, unique = true)
    private Customer customer;
}
