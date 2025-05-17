package com.vdt.vdt.dto;

import lombok.Data;

@Data
public class AgentSlaBreachDTO {
    private String agent;
    private Long breachedTicketCount;

    public AgentSlaBreachDTO(String agent, Long breachedTicketCount) {
        this.agent = agent;
        this.breachedTicketCount = breachedTicketCount;
    }

    public String getAgent() {
        return agent;
    }

    public Long getBreachedTicketCount() {
        return breachedTicketCount;
    }
}

