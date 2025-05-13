package com.vdt.vdt.dto;

import com.vdt.vdt.entity.EscalationLevel;
import lombok.Data;

import java.util.List;

@Data
public class CaseResponse {
    private Long caseId;
    private String caseTitle;
    private String caseType;
    private String caseStatus;
    private List<Long> ticketIds;
    private EscalationLevel escalationLevel;



    public EscalationLevel getEscalationLevel() {
        return escalationLevel;
    }

    public void setEscalationLevel(EscalationLevel escalationLevel) {
        this.escalationLevel = escalationLevel;
    }

    public Long getCaseId() {
        return caseId;
    }

    public void setCaseId(Long caseId) {
        this.caseId = caseId;
    }

    public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(String caseTitle) {
        this.caseTitle = caseTitle;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }



    public List<Long> getTicketIds() {
        return ticketIds;
    }

    public void setTicketIds(List<Long> ticketIds) {
        this.ticketIds = ticketIds;
    }


}
