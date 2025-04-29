package com.vdt.vdt.dto;

import com.vdt.vdt.entity.BillingCycle;
import com.vdt.vdt.entity.KYCPackageType;
import lombok.Data;

import java.util.List;

@Data
public class CustomerRequestDTO {

    Long userAgentId;
    Long tenantId;
    String name;
    String phoneNumber;
    String email;
    String accountType;
    String customerTier;
    String accountNumber;

    List<Long> serviceTypeId;
    BillingCycle billingCycleType;
    KYCPackageType kycPackageType;

}
