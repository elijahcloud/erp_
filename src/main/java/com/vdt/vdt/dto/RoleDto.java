package com.vdt.vdt.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RoleDto {
    private Long id;
    private String name;
    private String description;
    private String type;
    private int userCount;
    private LocalDateTime createdAt;
}
