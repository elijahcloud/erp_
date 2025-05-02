package com.vdt.vdt.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vdt.vdt.dto.UserCreateRequest;
import com.vdt.vdt.entity.Role;
import com.vdt.vdt.entity.Tenant;
import com.vdt.vdt.entity.User;
import com.vdt.vdt.entity.UserRole;
import com.vdt.vdt.entity.UserTenant;
import com.vdt.vdt.repository.RoleRepository;
import com.vdt.vdt.repository.UserRepository;
import com.vdt.vdt.repository.UserRoleRepository;
import com.vdt.vdt.repository.UserTenantRepository;
import com.vdt.vdt.repository.TicketRepository;
import com.vdt.vdt.entity.Ticket;
import com.vdt.vdt.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserTenantRepository userTenantRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final TicketRepository ticketRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public void addUserToTenant(UserCreateRequest req, String authToken) {
        Long creatorId = jwtUtil.extractUserId(authToken);
        if (creatorId == null) {
            throw new RuntimeException("Invalid authentication token.");
        }

        User creator = userRepository.findById(String.valueOf(creatorId))
                .orElseThrow(() -> new RuntimeException("Creator not found."));

        User user = userRepository.findByEmail(req.getEmail())
                .orElseGet(() -> createNewUser(req.getEmail(), creator));

        if (userTenantRepository.existsByUserIdAndTenantId(user.getId(), req.getTenantId())) {
            throw new RuntimeException("User already exists in this tenant.");
        }

        associateUserWithTenant(user, req.getTenantId());
        assignRolesToUser(user, req.getRoleIds());
    }

    private User createNewUser(String email, User creator) {
        User user = User.builder()
                .email(email)
                .password(null)
                .isActive(true)
                .createdBy(creator)
                .createdAt(LocalDateTime.now(ZoneId.of("Africa/Lagos")))
                .build();
        return userRepository.save(user);
    }

    private void associateUserWithTenant(User user, Long tenantId) {
        UserTenant userTenant = UserTenant.builder()
                .user(user)
                .tenant(new Tenant(tenantId))
                .isPrimary(false)
                .createdAt(LocalDateTime.now(ZoneId.of("Africa/Lagos")))
                .build();
        userTenantRepository.save(userTenant);
    }

    private void assignRolesToUser(User user, List<Long> roleIds) {
        List<Role> roles = roleRepository.findAllById(roleIds);
        if (roles.isEmpty()) {
            throw new RuntimeException("No valid roles found for the provided role IDs.");
        }

        Set<UserRole> userRoles = new HashSet<>();
        for (Role role : roles) {
            UserRole userRole = UserRole.builder()
                    .user(user)
                    .role(role)
                    .build();
            userRoleRepository.save(userRole);
            userRoles.add(userRole);
        }

        user.setActiveRole(userRoles.iterator().next().getRole().getId());
        userRepository.save(user);
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
        return userRepository.findById(String.valueOf(assignedAgent.getId()));
    }

    public Optional<User> findById(Long agentId) {
        return userRepository.findById(String.valueOf(agentId));
    }
}