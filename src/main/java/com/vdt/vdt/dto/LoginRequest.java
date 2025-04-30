package com.vdt.vdt.dto;

import lombok.Data;
import lombok.Getter;


public class LoginRequest {
    private String email;
    private String password;
    private String tenant;
    private String preLoginToken; // Add this field

    // Getters and setters...

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getTenant() {
        return tenant;
    }

    public String getPreLoginToken() {
        return preLoginToken;
    }

    public void setPreLoginToken(String preLoginToken) {
        this.preLoginToken = preLoginToken;
    }
}

