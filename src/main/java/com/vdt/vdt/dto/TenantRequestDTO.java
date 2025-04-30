package com.vdt.vdt.dto;

import lombok.Data;

@Data
public class TenantRequestDTO {
    private String tenantName;
    private String tenantId;
    private String adminEmail;
    private String adminName;
    private String alias;
    private Long sectorId;
    private Long subSectorId;
    private String phoneNumber;
    private String description;


    // Getter for tenantName
    public String getTenantName() {
        return tenantName; // Ensure this matches the field name
    }

    // Setter for tenantName
    public void setTenantName(String tenantName) {
        this.tenantName = tenantName; // Ensure this matches the field name
    }

    // Getter for tenantId
    public String getTenantId() {
        return tenantId;
    }

    // Setter for tenantId
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}