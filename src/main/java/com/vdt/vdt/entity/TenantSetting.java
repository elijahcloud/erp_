package com.vdt.vdt.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "tenant_setting")
public class TenantSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_setting_id")
    private Long tenantSettingId;

    @Column(name = "tenant_setting_tenant_id", nullable = false)
    private Long tenantSettingTenantId;

    @Column(name = "tenant_setting_min_password_length")
    @Builder.Default
    private Integer tenantSettingMinPasswordLength = 8;

    @Column(name = "tenant_setting_require_special_characters")
    @Builder.Default
    private Boolean tenantSettingRequireSpecialCharacters = false;

    @Column(name = "tenant_setting_require_numbers")
    @Builder.Default
    private Boolean tenantSettingRequireNumbers = false;

    @Column(name = "tenant_setting_require_uppercase_letters")
    @Builder.Default
    private Boolean tenantSettingRequireUppercaseLetters = false;

    @Column(name = "tenant_setting_session_timeout_minutes")
    @Builder.Default
    private Integer tenantSettingSessionTimeoutMinutes = 30;

    @Column(name = "tenant_setting_require_mfa")
    @Builder.Default
    private Boolean tenantSettingRequireMfa = false;

    @Column(name = "tenant_setting_dark_mode_enabled")
    @Builder.Default
    private Boolean tenantSettingDarkModeEnabled = false;

    @Column(name = "tenant_setting_created_at", updatable = false)
    private LocalDateTime tenantSettingCreatedAt;

    @Column(name = "tenant_setting_updated_at")
    private LocalDateTime tenantSettingUpdatedAt;

    @PrePersist
    protected void onCreate() {
        tenantSettingCreatedAt = LocalDateTime.now();
        tenantSettingUpdatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        tenantSettingUpdatedAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getTenantSettingId() {
        return tenantSettingId;
    }

    public void setTenantSettingId(Long tenantSettingId) {
        this.tenantSettingId = tenantSettingId;
    }

    public Long getTenantSettingTenantId() {
        return tenantSettingTenantId;
    }

    public void setTenantSettingTenantId(Long tenantSettingTenantId) {
        this.tenantSettingTenantId = tenantSettingTenantId;
    }

    public Integer getTenantSettingMinPasswordLength() {
        return tenantSettingMinPasswordLength;
    }

    public void setTenantSettingMinPasswordLength(Integer tenantSettingMinPasswordLength) {
        this.tenantSettingMinPasswordLength = tenantSettingMinPasswordLength;
    }

    public Boolean getTenantSettingRequireSpecialCharacters() {
        return tenantSettingRequireSpecialCharacters;
    }

    public void setTenantSettingRequireSpecialCharacters(Boolean tenantSettingRequireSpecialCharacters) {
        this.tenantSettingRequireSpecialCharacters = tenantSettingRequireSpecialCharacters;
    }

    public Boolean getTenantSettingRequireNumbers() {
        return tenantSettingRequireNumbers;
    }

    public void setTenantSettingRequireNumbers(Boolean tenantSettingRequireNumbers) {
        this.tenantSettingRequireNumbers = tenantSettingRequireNumbers;
    }

    public Boolean getTenantSettingRequireUppercaseLetters() {
        return tenantSettingRequireUppercaseLetters;
    }

    public void setTenantSettingRequireUppercaseLetters(Boolean tenantSettingRequireUppercaseLetters) {
        this.tenantSettingRequireUppercaseLetters = tenantSettingRequireUppercaseLetters;
    }

    public Integer getTenantSettingSessionTimeoutMinutes() {
        return tenantSettingSessionTimeoutMinutes;
    }

    public void setTenantSettingSessionTimeoutMinutes(Integer tenantSettingSessionTimeoutMinutes) {
        this.tenantSettingSessionTimeoutMinutes = tenantSettingSessionTimeoutMinutes;
    }

    public Boolean getTenantSettingRequireMfa() {
        return tenantSettingRequireMfa;
    }

    public void setTenantSettingRequireMfa(Boolean tenantSettingRequireMfa) {
        this.tenantSettingRequireMfa = tenantSettingRequireMfa;
    }

    public Boolean getTenantSettingDarkModeEnabled() {
        return tenantSettingDarkModeEnabled;
    }

    public void setTenantSettingDarkModeEnabled(Boolean tenantSettingDarkModeEnabled) {
        this.tenantSettingDarkModeEnabled = tenantSettingDarkModeEnabled;
    }

    public LocalDateTime getTenantSettingCreatedAt() {
        return tenantSettingCreatedAt;
    }

    public void setTenantSettingCreatedAt(LocalDateTime tenantSettingCreatedAt) {
        this.tenantSettingCreatedAt = tenantSettingCreatedAt;
    }

    public LocalDateTime getTenantSettingUpdatedAt() {
        return tenantSettingUpdatedAt;
    }

    public void setTenantSettingUpdatedAt(LocalDateTime tenantSettingUpdatedAt) {
        this.tenantSettingUpdatedAt = tenantSettingUpdatedAt;
    }
}

