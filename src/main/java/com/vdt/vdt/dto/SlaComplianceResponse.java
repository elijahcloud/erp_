package com.vdt.vdt.dto;

import lombok.Data;

@Data
public class SlaComplianceResponse {
    private Long caseId;
    private double compliancePercentage;

    public SlaComplianceResponse(Long caseId, double compliancePercentage) {
        this.caseId = caseId;
        this.compliancePercentage = compliancePercentage;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public double getCompliancePercentage() {
        return compliancePercentage;
    }

    public void setCompliancePercentage(double compliancePercentage) {
        this.compliancePercentage = compliancePercentage;
    }
}
