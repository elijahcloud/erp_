package com.vdt.vdt.dto;

import com.vdt.vdt.entity.TicketPriority;
import com.vdt.vdt.entity.TicketType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SlaPolicyResponse {

    private Long id;
    private TicketType ticketType;
    private TicketPriority priority;
    private String customerGroup;
    private Long responseTimeTargetMinutes;
    private Long resolutionTimeTargetMinutes;

    private Boolean escalateToManager;
    private Boolean reassignIfNoResponse;
    private Boolean alertSupervisorIfCritical;
    private Integer reassignThresholdHours;

    private boolean active;
    private LocalDateTime createdAt;
}

