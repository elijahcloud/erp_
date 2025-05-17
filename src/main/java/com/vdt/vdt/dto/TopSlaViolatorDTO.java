package com.vdt.vdt.dto;

import lombok.Data;

@Data
public class TopSlaViolatorDTO {
    private String agentEmail;
    private Long breachCount;

    public TopSlaViolatorDTO(String agentEmail, Long breachCount) {
        this.agentEmail = agentEmail;
        this.breachCount = breachCount;
    }

    public String getAgentEmail() {
        return agentEmail;
    }

    public void setAgentEmail(String agentEmail) {
        this.agentEmail = agentEmail;
    }

    public Long getBreachCount() {
        return breachCount;
    }

    public void setBreachCount(Long breachCount) {
        this.breachCount = breachCount;
    }
}

