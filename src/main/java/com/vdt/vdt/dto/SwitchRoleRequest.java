package com.vdt.vdt.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class SwitchRoleRequest {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Role ID is required")
    private String roleId;
}