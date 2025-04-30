package com.vdt.vdt.dto;

import com.vdt.vdt.entity.BillingCycle;
import lombok.Data;

@Data
public class ServiceSubscriptionRequestDto {
    Long serviceTypeId;
    Long tenantId;
    BillingCycle billingCycle;
}
