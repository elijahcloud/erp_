package com.vdt.vdt.util;

import com.vdt.vdt.entity.User;
import com.vdt.vdt.entity.Role;
import com.vdt.vdt.entity.Tenant;
import com.vdt.vdt.repository.UserRepository;
import com.vdt.vdt.repository.RoleRepository;
import com.vdt.vdt.repository.TenantRepository;
import com.vdt.vdt.entity.UserRole; // Ensure UserRole is imported
import com.vdt.vdt.repository.UserRoleRepository; // Ensure UserRoleRepository is imported

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.time.LocalDateTime;

@Component
public class Seeder {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private UserRoleRepository userRoleRepository; // Ensure UserRoleRepository is autowired

    @EventListener(ContextRefreshedEvent.class)
    public void seedPlatformSuperAdmin(ContextRefreshedEvent event) {
        // Ensure the "platform_super_admin" role exists
        Role adminRole = roleRepository.findByName("Platform Administrator")
                .orElseThrow(() -> new IllegalStateException("Platform Administrator role not found. Ensure roles are seeded in the database."));

        // Ensure the landlord tenant (VDT) exists
        Tenant landlordTenant = tenantRepository.findByName("VDT")
                .orElseGet(() -> {
                    Tenant newTenant = new Tenant();
                    newTenant.setName("VDT");
                    newTenant.setCode("VDT001");
                    newTenant.setAsLandlord(); // Use the new method
                    newTenant.setCreatedAt(LocalDateTime.now());
                    return tenantRepository.save(newTenant);
                });

        // Ensure the platform admin user exists and is assigned the "platform_super_admin" role
        userRepository.findByEmail("opeoluwa007@gmail.com").orElseGet(() -> {
//            User adminUser = new User();
//            adminUser.setEmail("opeoluwa007@gmail.com");
//            adminUser.setPassword(PasswordUtil.hashPassword("admin123")); // Default password

            User adminUser = new User("opeoluwa007@gmail.com", PasswordUtil.hashPassword("admin123"));
            // Save the adminUser first to generate its ID
            adminUser = userRepository.save(adminUser);

            // Create and assign UserRole
            UserRole userRole = new UserRole();
            userRole.setUser(adminUser);
            userRole.setRole(adminRole);
            userRole.setTenant(landlordTenant);
            userRole.setCreatedBy(adminUser); // Set createdBy to the saved admin user
            userRole.setCreatedAt(LocalDateTime.now()); // Set createdAt timestamp

            // Save UserRole to ensure ID is generated
            userRole = userRoleRepository.save(userRole);

            // Reload the adminUser to ensure it is fully managed
            adminUser = userRepository.findById(String.valueOf(adminUser.getId()))
                    .orElseThrow(() -> new IllegalStateException("Admin user not found after save."));

            // Add role to user after UserRole is saved
            adminUser.getRoles().add(userRole);

            // Set active role after UserRole is saved
            adminUser.setActiveRole(userRole.getId());

            // Save the adminUser again to persist changes
            return userRepository.save(adminUser);
        });

        System.out.println("Seeded platform administrator user and landlord tenant.");
    }
}
