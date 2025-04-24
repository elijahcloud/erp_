package com.vdt.vdt.dto;

import lombok.Data;
import java.util.List;

@Data
public class LoginResponse {
    private String id; // Add user ID
    private String email;
    private String name;
    private List<UserRoleResponse> roles;
    private UserRoleResponse activeRole; // Add active role
    private Boolean isPlatformAdmin; // Add platform admin flag
    private String token;
}
