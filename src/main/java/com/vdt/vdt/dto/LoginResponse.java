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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserRoleResponse> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRoleResponse> roles) {
        this.roles = roles;
    }

    public UserRoleResponse getActiveRole() {
        return activeRole;
    }

    public void setActiveRole(UserRoleResponse activeRole) {
        this.activeRole = activeRole;
    }

    public Boolean getIsPlatformAdmin() {
        return isPlatformAdmin;
    }

    public void setIsPlatformAdmin(Boolean platformAdmin) {
        isPlatformAdmin = platformAdmin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
