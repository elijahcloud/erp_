package com.vdt.vdt.dto;

import lombok.Data;

@Data
public class ERPServiceDTO {
    private Long id;
    private Long tenantId;
    private String serviceName;
    private String linkedBranch;
    private String linkedZone;
    private Double price;
}
