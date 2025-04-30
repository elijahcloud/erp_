package com.vdt.vdt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ERPServiceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "erp_service_id")
    private Long id;

    private String serviceName;

    private String linkedBranch;
    private String linkedZone;

    @ToString.Exclude
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "tenant_id_ref", referencedColumnName = "tenant_id")
    Tenant tenant;

    @OneToMany(mappedBy = "erpServiceType")
    List<Price> priceHistory;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "erpServiceType")
    List<SubscriptionService> subscriptionServices;
}
