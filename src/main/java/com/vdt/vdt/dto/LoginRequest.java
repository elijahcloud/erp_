package com.vdt.vdt.dto;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String email;
    private String password;
    private String tenant;
    private String preLoginToken; // Add this field

    // Getters and setters...

    public String getPreLoginToken() {
        return preLoginToken;
    }

    public void setPreLoginToken(String preLoginToken) {
        this.preLoginToken = preLoginToken;
    }
}

