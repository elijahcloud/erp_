package com.vdt.vdt.dto;

import lombok.Data;

@Data
public class SlaBreachStatusDTO {
    private String remainingTime;
    private String slaStatus;

    public SlaBreachStatusDTO(String remainingTime, String slaStatus) {
        this.remainingTime = remainingTime;
        this.slaStatus = slaStatus;
    }


    public String getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(String remainingTime) {
        this.remainingTime = remainingTime;
    }

    public String getSlaStatus() {
        return slaStatus;
    }

    public void setSlaStatus(String slaStatus) {
        this.slaStatus = slaStatus;
    }
}

