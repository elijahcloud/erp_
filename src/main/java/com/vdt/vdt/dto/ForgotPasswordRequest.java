package com.vdt.vdt.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class ForgotPasswordRequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Tenant is required")
    private String tenant;

    // Getter for email
    public String getEmail() {
        return email;
    }

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter for tenant
    public String getTenant() {
        return tenant;
    }

    // Setter for tenant
    public void setTenant(String tenant) {
        this.tenant = tenant;
    }
}