package com.vdt.vdt.dto;


import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class TenantGeneralSettingsDTO {
    private Long tenantId; // Added tenantId field
    private String organizationName;
    private String tenantDomain;
    private String passwordMinLength;
    private Boolean passwordRequireSpecial;
    private Boolean passwordRequireNumbers;
    private Boolean passwordRequireUppercase;
    private String sessionTimeout;
    private Boolean mfaEnabled;
    private Boolean darkMode;
    private String tenantLogo;
    private MultipartFile logoFile;

    // Getters and setters for all fields
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getTenantDomain() {
        return tenantDomain;
    }

    public void setTenantDomain(String tenantDomain) {
        this.tenantDomain = tenantDomain;
    }

    public String getPasswordMinLength() {
        return passwordMinLength;
    }

    public void setPasswordMinLength(String passwordMinLength) {
        this.passwordMinLength = passwordMinLength;
    }

    public Boolean getPasswordRequireSpecial() {
        return passwordRequireSpecial;
    }

    public void setPasswordRequireSpecial(Boolean passwordRequireSpecial) {
        this.passwordRequireSpecial = passwordRequireSpecial;
    }

    public Boolean getPasswordRequireNumbers() {
        return passwordRequireNumbers;
    }

    public void setPasswordRequireNumbers(Boolean passwordRequireNumbers) {
        this.passwordRequireNumbers = passwordRequireNumbers;
    }

    public Boolean getPasswordRequireUppercase() {
        return passwordRequireUppercase;
    }

    public void setPasswordRequireUppercase(Boolean passwordRequireUppercase) {
        this.passwordRequireUppercase = passwordRequireUppercase;
    }

    public String getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(String sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public Boolean getMfaEnabled() {
        return mfaEnabled;
    }

    public void setMfaEnabled(Boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }

    public Boolean getDarkMode() {
        return darkMode;
    }

    public void setDarkMode(Boolean darkMode) {
        this.darkMode = darkMode;
    }

    public String getTenantLogo() {
        return tenantLogo;
    }

    public void setTenantLogo(String tenantLogo) {
        this.tenantLogo = tenantLogo;
    }

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    
}

