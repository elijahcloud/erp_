package com.vdt.vdt.service;

import com.vdt.vdt.dto.SlaPolicyRequest;
import com.vdt.vdt.dto.SlaPolicyResponse;
import com.vdt.vdt.entity.*;
import com.vdt.vdt.repository.SlaPolicyRepository;
import com.vdt.vdt.repository.TicketRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SlaPolicyService {

    private final SlaPolicyRepository slaPolicyRepository;
    private final ModelMapper slaPolicyMapper;
    private final TicketRepository ticketRepository;
    private static final Logger log = LoggerFactory.getLogger(SlaPolicyService.class);

    public SlaPolicyService(SlaPolicyRepository slaPolicyRepository, ModelMapper slaPolicyMapper, TicketRepository ticketRepository) {
        this.slaPolicyRepository = slaPolicyRepository;
        this.slaPolicyMapper = slaPolicyMapper;
        this.ticketRepository = ticketRepository;
    }

    public Page<SlaPolicyResponse> getAllPolicies(Pageable pageable) {
        Page<SlaPolicy> policies = slaPolicyRepository.findAll(pageable);
        return policies.map(policy -> slaPolicyMapper.map(policy, SlaPolicyResponse.class));
    }

    public SlaPolicyResponse createPolicy(SlaPolicyRequest request) {
        SlaPolicy slaPolicy = slaPolicyMapper.map(request,SlaPolicy.class);
        slaPolicy.setTicketType(TicketType.valueOf(request.getTicketType()));
        slaPolicy.setCreatedAt(LocalDateTime.now());
        slaPolicy.setUpdatedAt(LocalDateTime.now());
        SlaPolicy saved = slaPolicyRepository.save(slaPolicy);
        return slaPolicyMapper.map(saved,SlaPolicyResponse.class);
    }

    public SlaPolicyResponse updatePolicy(Long id, SlaPolicyRequest request) {
        SlaPolicy slaPolicy = slaPolicyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SLA Policy with id " + id + " not found"));

        applyPartialUpdate(slaPolicy, request);

        slaPolicy.setUpdatedAt(LocalDateTime.now());
        SlaPolicy updated = slaPolicyRepository.save(slaPolicy);

        return slaPolicyMapper.map(updated, SlaPolicyResponse.class);
    }


    private void applyPartialUpdate(SlaPolicy slaPolicy, SlaPolicyRequest request) {
        Optional.ofNullable(request.getTicketType())
                .map(String::toUpperCase)
                .map(TicketType::valueOf)
                .ifPresent(slaPolicy::setTicketType);

        Optional.ofNullable(request.getPriority())
                .map(String::toUpperCase)
                .map(TicketPriority::valueOf)
                .ifPresent(slaPolicy::setPriority);

        Optional.ofNullable(request.getCustomerGroup())
                .map(String::toUpperCase)
                .map(CustomerAccountType::valueOf)
                .ifPresent(slaPolicy::setCustomerGroup);
        if (request.getResponseTimeTargetInMinutes() > 0) {
            slaPolicy.setResponseTimeTargetInMinutes(request.getResponseTimeTargetInMinutes());
        }


        if (request.getResolutionTimeTargetInMinutes() > 0) {
            slaPolicy.setResolutionTimeTargetInMinutes(request.getResolutionTimeTargetInMinutes());
        }

        Optional.ofNullable(request.getReassignThresholdHours())
                .ifPresent(slaPolicy::setReassignThresholdHours);

    }
    public void deletePolicy(Long id) {
        SlaPolicy slaPolicy = slaPolicyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SLA Policy with id " + id + " not found"));

        slaPolicyRepository.delete(slaPolicy);
    }

    public double calculateSlaBreachPercentage(Long slaPolicyId, LocalDateTime ticketCreationTime) {
        SlaPolicy slaPolicy = slaPolicyRepository.findById(slaPolicyId)
                .orElseThrow(() -> new RuntimeException("SLA Policy with id " + slaPolicyId + " not found"));

        Duration elapsedTime = Duration.between(ticketCreationTime, LocalDateTime.now());

        long responseTimeTarget = slaPolicy.getResponseTimeTargetInMinutes();
        long resolutionTimeTarget = slaPolicy.getResolutionTimeTargetInMinutes();

        double responsePercentage = calculatePercentage(elapsedTime, Duration.ofDays(responseTimeTarget));
        double resolutionPercentage = calculatePercentage(elapsedTime, Duration.ofDays(resolutionTimeTarget));

        return Math.max(responsePercentage, resolutionPercentage);
    }

    private double calculatePercentage(Duration elapsedTime, Duration slaTime) {
        if (elapsedTime.isNegative()) {
            return 0;
        }
        double percentage = (elapsedTime.toMinutes() / (double) slaTime.toMinutes()) * 100;
        return Math.min(percentage, 100);
    }

    public SlaPolicy getSlaPolicyForTicket(long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket with id " + ticketId + " not found"));
        return slaPolicyRepository.findByCustomerGroupAndTicketType(ticket.getCustomer().getAccountType(), ticket.getType());
    }

    public SlaPolicyResponse getPolicyById(Long id) {
        SlaPolicy slaPolicy = slaPolicyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SLA Policy with ID " + id + " not found"));
        return slaPolicyMapper.map(slaPolicy, SlaPolicyResponse.class);
    }
}
