package com.vdt.vdt.service;

import com.vdt.vdt.entity.Ticket;
import com.vdt.vdt.entity.User;
import com.vdt.vdt.entity.UserRole;
import com.vdt.vdt.repository.TicketRepository;
import com.vdt.vdt.repository.UserRepository;
import com.vdt.vdt.repository.UserRoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service

public class UserService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public UserService(UserRepository userRepository, TicketRepository ticketRepository,
                        UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
        this.userRoleRepository = userRoleRepository;
    }
    @Transactional(readOnly = true)
    public User getManagerOfUserAssignedToATicket(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .map(Ticket::getAssignedAgent)
                .map(User::getCreatedBy)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public User getCrmSupervisor() {
        var crmSupervisor = userRoleRepository.findFirstByRoleNameIgnoreCase("CRM Supervisor");
        return crmSupervisor.map(UserRole::getUser).orElse(null);
    }


    public Optional<User> findByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }



    public Optional<User> findByUser(User assignedAgent) {

        return userRepository.findById(assignedAgent.getId());
    }

    public Optional<User> findById(Long agentId) {
        return userRepository.findById(agentId);
    }
}
