package com.vdt.vdt.service;

import com.vdt.vdt.dto.TenantRequestDTO;
import com.vdt.vdt.entity.Tenant;
import com.vdt.vdt.entity.User;
import com.vdt.vdt.entity.Role;
import com.vdt.vdt.entity.UserRole;
import com.vdt.vdt.entity.UserTenant;
import com.vdt.vdt.repository.RoleRepository;
import com.vdt.vdt.repository.TenantRepository;
import com.vdt.vdt.repository.UserRepository;
import com.vdt.vdt.repository.UserRoleRepository;
import com.vdt.vdt.repository.UserTenantRepository;
import com.vdt.vdt.util.JwtUtil;
import com.vdt.vdt.util.PasswordUtil;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TenantService {

    @Autowired
    private TenantRepository tenantRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private UserTenantRepository userTenantRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private UserRoleRepository userRoleRepository;
    @Autowired private JwtUtil jwtUtil;

    public long getActiveTenantCount() {
        // Fetch and return the count of active tenants excluding those with tenant_code as VDT001
        long activeTenantCount = tenantRepository.countByIsActiveTrueAndCodeNot("VDT001");
        System.out.println("Active tenant count: " + activeTenantCount); // Debugging
        return activeTenantCount;
    }

    public void createTenant(TenantRequestDTO request, String authHeader) {
        // Extract user ID from token using JwtUtil
        Long createdByUserId = jwtUtil.extractUserId(authHeader);
        if (createdByUserId == null) {
            throw new RuntimeException("Invalid authentication token.");
        }

        User createdByUser = userRepository.findById(createdByUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Tenant> existingTenant = tenantRepository.findByName(request.getTenantName());
        if (existingTenant.isPresent()) {
            throw new RuntimeException("Tenant with this name already exists.");
        }

        // Create Tenant
        Tenant tenant = new Tenant();
        tenant.setName(request.getTenantName());
        tenant.setCode(request.getTenantId()); // Updated to use getTenantId()
        tenant.setAlias(request.getAlias());
        tenant.setPhoneNumber(request.getPhoneNumber());
        tenant.setDescription(request.getDescription());
        tenant.setSectorId(request.getSectorId());
        tenant.setSubSectorId(request.getSubSectorId());
        tenant.setIsActive(true);
        tenant.setCreatedAt(LocalDateTime.now());
        tenant.setCreatedBy(createdByUser);
        tenantRepository.save(tenant);

        // Check for existing admin user
        Optional<User> optionalAdmin = userRepository.findByEmail(request.getAdminEmail());
        User adminUser;

        if (optionalAdmin.isEmpty()) {
            adminUser = new User();
            adminUser.setEmail(request.getAdminEmail());
            adminUser.setPassword(PasswordUtil.hashPassword("Default@123")); // Use PasswordUtil
            adminUser.setIsActive(true);
            adminUser.setCreatedAt(LocalDateTime.now());
            adminUser.setCreatedBy(createdByUser);
            userRepository.save(adminUser);
        } else {
            adminUser = optionalAdmin.get();
        }

        // Associate User with Tenant
        if (!userTenantRepository.existsByUserIdAndTenantId(adminUser.getId(), tenant.getId())) {
            UserTenant userTenant = new UserTenant();
            userTenant.setTenant(tenant);
            userTenant.setUser(adminUser);
            userTenant.setCreatedAt(LocalDateTime.now());
            userTenant.setCreatedBy(createdByUser);
            userTenantRepository.save(userTenant);
        }

        // Assign Tenant Admin Role
        Role tenantAdminRole = roleRepository.findByName("Tenant Admin")
            .orElseThrow(() -> new RuntimeException("Tenant Admin role not found."));

        if (!userRoleRepository.existsByUserIdAndTenantIdAndRoleId(adminUser.getId(), tenant.getId(), tenantAdminRole.getId())) {
            UserRole userRole = new UserRole();
            userRole.setUser(adminUser);
            userRole.setTenant(tenant);
            userRole.setRole(tenantAdminRole);
            userRole.setCreatedAt(LocalDateTime.now());
            userRole.setCreatedBy(createdByUser);
            userRoleRepository.save(userRole);
        }
    }

    private String generateTenantCode(String name) {
        String baseCode = name.replaceAll("[^a-zA-Z ]", "").trim() // Remove non-alphabetic characters
                              .replaceAll("\\s+", " ") // Normalize spaces
                              .toUpperCase()
                              .chars()
                              .filter(Character::isLetter)
                              .mapToObj(c -> String.valueOf((char) c))
                              .reduce("", (acc, c) -> acc + c.charAt(0));
        int suffix = 1;
        String tenantCode;

        do {
            tenantCode = baseCode + String.format("%03d", suffix);
            suffix++;
        } while (tenantRepository.existsByCode(tenantCode));

        return tenantCode;
    }
}
