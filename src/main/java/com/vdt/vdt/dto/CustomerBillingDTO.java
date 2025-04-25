package com.vdt.vdt.dto;

import lombok.Data;

@Data
public class CustomerBillingDTO {
    private String paymentMethod;
    private String billingCycle;
    private String currency;
    private String taxId;
}
