package com.vdt.vdt.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateCaseRequest {
    private String caseTitle;
    private String caseType;

    private List<Long> ticketIds;


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


    public List<Long> getTicketIds() {
        return ticketIds;
    }

    public void setTicketIds(List<Long> ticketIds) {
        this.ticketIds = ticketIds;
    }

}
