package com.vdt.vdt.dto;

import com.vdt.vdt.entity.Ticket;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class TicketDetailDto {
    private Long id;
    private String ticketTitle;
    private String type;
    private String description;
    private String priority;
    private String status;
    private String source;
    private String assignedAgent;
    private String customerName;
    private String customerDepartment;
    private String resolvingDepartment;
    private String assignedTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentDto> comments;

    public TicketDetailDto(Ticket ticket) {
        this.id = ticket.getId();
        this.ticketTitle = ticket.getTicketTitle();
        this.description = ticket.getDescription();
        this.status = String.valueOf(ticket.getStatus());
        this.priority = String.valueOf(ticket.getPriority());
        this.assignedAgent = ticket.getAssignedAgent();
        this.customerName = ticket.getCustomer() != null ? ticket.getCustomer().getCustomerName() : null;
        this.createdAt = ticket.getCreatedAt();

        this.customerDepartment = ticket.getCustomer() != null && ticket.getCustomer().getDepartment() != null
                ? ticket.getCustomer().getDepartment().getName()
                : "N/A";

        this.assignedTo = ticket.getAssignedAgent();

        this.resolvingDepartment = ticket.getAssignedDepartment() != null
                ? ticket.getAssignedDepartment().getName()
                : "Unassigned";
    }
}
