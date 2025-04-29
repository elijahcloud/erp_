package com.vdt.vdt.service;

import com.vdt.vdt.dto.SlaPolicyRequest;
import com.vdt.vdt.dto.SlaPolicyResponse;
import com.vdt.vdt.entity.SlaPolicy;
import com.vdt.vdt.entity.Ticket;
import com.vdt.vdt.repository.SlaPolicyRepository;
import com.vdt.vdt.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SlaPolicyService {

    private final SlaPolicyRepository slaPolicyRepository;
    private final ModelMapper slaPolicyMapper;
    private final TicketRepository ticketRepository;

    public SlaPolicyService(SlaPolicyRepository slaPolicyRepository, ModelMapper slaPolicyMapper, TicketRepository ticketRepository) {
        this.slaPolicyRepository = slaPolicyRepository;
        this.slaPolicyMapper = slaPolicyMapper;
        this.ticketRepository = ticketRepository;
    }

    public List<SlaPolicyResponse> getAllPolicies() {
        List<SlaPolicy> policies = slaPolicyRepository.findAll();
        return policies.stream()
                .map(policy -> slaPolicyMapper.map(policy, SlaPolicyResponse.class))
                .collect(Collectors.toList());
    }

    public SlaPolicyResponse createPolicy(SlaPolicyRequest request) {

        SlaPolicy slaPolicy = slaPolicyMapper.map(request,SlaPolicy.class);
        slaPolicy.setResponseTimeTarget(Duration.ofMinutes(request.getResponseTimeTargetMinutes()));
        slaPolicy.setResolutionTimeTarget(Duration.ofMinutes(request.getResolutionTimeTargetMinutes()));
        slaPolicy.setCreatedAt(LocalDateTime.now());
        slaPolicy.setUpdatedAt(LocalDateTime.now());
        SlaPolicy saved = slaPolicyRepository.save(slaPolicy);
        return slaPolicyMapper.map(saved,SlaPolicyResponse.class);
    }

    public SlaPolicyResponse updatePolicy(Long id, SlaPolicyRequest request) {
        SlaPolicy slaPolicy = slaPolicyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SLA Policy with id " + id + " not found"));

        slaPolicyMapper.map(request, slaPolicy);;
        slaPolicy.setUpdatedAt(LocalDateTime.now());

        SlaPolicy updated = slaPolicyRepository.save(slaPolicy);
        return slaPolicyMapper.map(updated, SlaPolicyResponse.class);
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

        Duration responseTimeTarget = slaPolicy.getResponseTimeTarget();
        Duration resolutionTimeTarget = slaPolicy.getResolutionTimeTarget();

        double responsePercentage = calculatePercentage(elapsedTime, responseTimeTarget);
        double resolutionPercentage = calculatePercentage(elapsedTime, resolutionTimeTarget);

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
}
