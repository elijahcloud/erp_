package com.vdt.vdt.dto;

import lombok.Data;

@Data
public class CreateTicketRequest {
    private String ticketTitle;
    private String type;
    private String description;
    private String priority;
    private Long customerId;
    private String assignedAgent;
}
