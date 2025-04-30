package com.vdt.vdt.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class SwitchRoleRequest {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Role ID is required")
    private String roleId;

    public @NotBlank(message = "User ID is required") String getUserId() {
        return userId;
    }

    public void setUserId(@NotBlank(message = "User ID is required") String userId) {
        this.userId = userId;
    }

    public @NotBlank(message = "Role ID is required") String getRoleId() {
        return roleId;
    }

    public void setRoleId(@NotBlank(message = "Role ID is required") String roleId) {
        this.roleId = roleId;
    }
}