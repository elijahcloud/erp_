package com.vdt.vdt.service;

import com.vdt.vdt.dto.CommentDto;
import com.vdt.vdt.dto.CreateTicketRequest;
import com.vdt.vdt.dto.TicketDetailDto;
import com.vdt.vdt.entity.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TicketMapper {


    public Ticket toEntity(CreateTicketRequest dto, Customer customer) {
        Ticket ticket = new Ticket();
        ticket.setType(TicketType.valueOf(dto.getType()));
        ticket.setDescription(dto.getDescription());
        ticket.setPriority(TicketPriority.valueOf(dto.getPriority()));
        ticket.setSource("Manual");
        ticket.setAssignedAgent(dto.getAssignedAgent());
        ticket.setCustomer(customer);
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedAt(LocalDateTime.now());
        return ticket;
    }

    public TicketDetailDto toDto(Ticket ticket) {
        TicketDetailDto dto = new TicketDetailDto();

        dto.setId(ticket.getId());
        dto.setTicketTitle(ticket.getTicketTitle());
        dto.setDescription(ticket.getDescription());
        dto.setStatus(ticket.getStatus().name());
        dto.setPriority(ticket.getPriority().name());
        dto.setType(ticket.getType().name());
        dto.setSource(ticket.getSource());
        dto.setAssignedAgent(ticket.getAssignedAgent());
        dto.setCustomerName(ticket.getCustomer() != null ? ticket.getCustomer().getCustomerName() : null);
        dto.setCreatedAt(ticket.getCreatedAt());
        dto.setUpdatedAt(ticket.getUpdatedAt());

        dto.setCustomerDepartment(
                ticket.getCustomer() != null && ticket.getCustomer().getDepartment() != null
                        ? ticket.getCustomer().getDepartment().getName()
                        : "N/A"
        );

        dto.setAssignedTo(ticket.getAssignedAgent());

        dto.setResolvingDepartment(
                ticket.getAssignedDepartment() != null
                        ? ticket.getAssignedDepartment().getName()
                        : "Unassigned"
        );


        if (ticket.getComments() != null) {
            List<CommentDto> commentDtos = ticket.getComments().stream()
                    .map(comment -> {
                        CommentDto cd = new CommentDto();
                        cd.setId(comment.getId());
                        cd.setComment(comment.getComment());
                        cd.setAuthor(comment.getAuthor());
                        cd.setTimestamp(comment.getTimestamp());
                        return cd;
                    })
                    .collect(Collectors.toList());
            dto.setComments(commentDtos);
        }

        return dto;
    }



}
