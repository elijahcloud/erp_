package com.vdt.vdt.config;

import com.vdt.vdt.dto.SlaPolicyRequest;
import com.vdt.vdt.dto.TicketDetailDto;
import com.vdt.vdt.dto.TicketSlaDashboardDTO;
import com.vdt.vdt.entity.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
        ;
        TypeMap<SlaPolicyRequest, SlaPolicy> typeMap = modelMapper.createTypeMap(SlaPolicyRequest.class, SlaPolicy.class);
        typeMap.addMappings(mapper -> {
            mapper.map(src -> {
                String ticketType = src.getTicketType();
                return ticketType != null ? TicketType.valueOf(ticketType.toUpperCase()) : null;
            }, SlaPolicy::setTicketType);

            mapper.map(src -> {
                String priority = src.getPriority();
                return priority != null ? TicketPriority.valueOf(priority.toUpperCase()) : null;
            }, SlaPolicy::setPriority);

            mapper.map(src -> {
                String customerGroup = src.getCustomerGroup();
                return customerGroup != null ? CustomerAccountType.valueOf(customerGroup.toUpperCase()) : null;
            }, SlaPolicy::setCustomerGroup);
        });

        modelMapper.typeMap(Ticket.class, TicketDetailDto.class).addMappings(mapper -> {
            mapper.map(src -> {
                User agent = src.getAssignedAgent();
                return agent != null ? agent.getId() : null;
            }, TicketDetailDto::setAssignedAgent);
        });
        modelMapper.typeMap(Ticket.class, TicketSlaDashboardDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getPriority() != null ? src.getPriority().name() : null,
                    TicketSlaDashboardDTO::setPriority);
            mapper.map(src -> src.getTicketSlaStatus() != null ? src.getTicketSlaStatus().name() : null,
                    TicketSlaDashboardDTO::setSlaStatus);
        });
        return modelMapper;

    }
}