package com.vdt.vdt.dto;

import lombok.Data;

@Data
public class UserRoleResponse {
    private String id;
    private String role;
    private String tenantId;
    private String tenantName;
    private Boolean isPlatformAdmin;
}
