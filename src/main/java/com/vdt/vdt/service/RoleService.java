package com.vdt.vdt.service;

import com.vdt.vdt.dto.RoleDto;
import com.vdt.vdt.entity.Role;
import com.vdt.vdt.entity.RolePermission;
import com.vdt.vdt.entity.Permission;
import com.vdt.vdt.repository.RoleRepository;
import com.vdt.vdt.repository.RolePermissionRepository;
import com.vdt.vdt.util.JwtUtil;
import com.vdt.vdt.entity.User; 
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
// Importing List for handling collections
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository; // Add RolePermissionRepository
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;

    private RoleService(RoleRepository roleRepository, RolePermissionRepository rolePermissionRepository, JwtUtil jwtUtil, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
    }
    public ResponseEntity<?> getAllRoles(String authorizationHeader) {
        try {
            System.out.println("Fetching all roles with authorization header: " + authorizationHeader);

            // Extract and validate the token
            String token = null;
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            }
            System.out.println("Validating token: " + token);

            if (token == null || !jwtUtil.validateToken(token)) {
                System.out.println("Invalid or expired token: " + token);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or expired token");
            }

            // Fetch roles and map to DTOs
            List<RoleDto> roles = roleRepository.findAll().stream()
                .filter(role -> role.getIsDeleted() == null || !role.getIsDeleted())
                .map(this::mapToDto)
                .collect(Collectors.toList());

            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            System.out.println("Exception while fetching roles: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }


    public ResponseEntity<?> addRole(RoleDto roleDto, String authorizationHeader) {
        try {
            String token = null;
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
            }
    
            if (token == null || !jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or expired token");
            }
    
            // Check if the role already exists by name
            Optional<Role> existing = roleRepository.findByName(roleDto.getName());
            if (existing.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Role already exists");
            }
    
            Long creatorUserId = jwtUtil.extractUserId(token);
            User creator = new User();
            creator.setId(creatorUserId);
    
            Role role = Role.builder()
                    .name(roleDto.getName())
                    .description(roleDto.getDescription())
                    .scope(roleDto.getType())
                    .createdAt(LocalDateTime.now())
                    .createdBy(creator)
                    .isDeleted(false)
                    .build();
    
            Role savedRole = roleRepository.save(role);
    
            // Save permissions using RolePermission
            if (roleDto.getPermissionIds() != null && !roleDto.getPermissionIds().isEmpty()) {
                roleDto.getPermissionIds().forEach(permissionId -> {
                    RolePermission rolePermission = RolePermission.builder()
                        .role(savedRole)
                        .permission(new Permission(permissionId)) // Use the new constructor
                        .build();
                    rolePermissionRepository.save(rolePermission); // Use RolePermissionRepository
                });
            }
    
            return ResponseEntity.status(HttpStatus.CREATED).body("Role created successfully");
    
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create role");
        }
    }


    public ResponseEntity<?> editRole(RoleDto roleDto, String authorizationHeader) {
        try {
            // Step 1: Extract and validate token
            String token = null;
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
            }
    
            if (token == null || !jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or expired token");
            }
    
            // Step 2: Fetch the existing role
            Role role = roleRepository.findById(roleDto.getId())
                    .orElseThrow(() -> new RuntimeException("Role not found"));
    
            if (Boolean.TRUE.equals(role.getIsDeleted())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot edit a deleted role");
            }
    
            // Step 3: Check for uniqueness of new role name (if it's being changed)
            Optional<Role> existingRoleWithName = roleRepository.findByName(roleDto.getName());
            if (existingRoleWithName.isPresent() && !existingRoleWithName.get().getId().equals(role.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Role name already taken");
            }
    
            // Step 4: Update role fields
            Long updaterUserId = jwtUtil.extractUserId(token);
            User updater = new User();
            updater.setId(updaterUserId);
    
            role.setName(roleDto.getName());
            role.setDescription(roleDto.getDescription());
            role.setScope(roleDto.getType());
            role.setUpdatedBy(updater);
            role.setUpdatedAt(LocalDateTime.now());
    
            roleRepository.save(role);
    
            // Step 5: Reassign permissions using RolePermission
            rolePermissionRepository.deleteByRoleId(role.getId()); // Use RolePermissionRepository to clear existing permissions
    
            if (roleDto.getPermissionIds() != null && !roleDto.getPermissionIds().isEmpty()) {
                roleDto.getPermissionIds().forEach(permissionId -> {
                    RolePermission rolePermission = RolePermission.builder()
                        .role(role)
                        .permission(new Permission(permissionId)) // Use the new constructor
                        .build();
                    rolePermissionRepository.save(rolePermission); // Use RolePermissionRepository
                });
            }
    
            return ResponseEntity.ok("Role updated successfully");
    
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update role");
        }
    }
   

    public ResponseEntity<?> deleteRole(UUID id, String authorizationHeader) {
        try {
            String token = null;
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
            }

            if (token == null || !jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or expired token");
            }

            Long deleterUserId = jwtUtil.extractUserId(token);

            Role role = roleRepository.findById(Long.valueOf(id.toString()))
                    .orElseThrow(() -> new RuntimeException("Role not found"));

            if (role.getUserCount() != null && role.getUserCount() > 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Cannot delete role assigned to users");
            }

            role.setDeletedAt(LocalDateTime.now());
            role.setIsDeleted(true);

            User deleter = new User();
            deleter.setId(deleterUserId);
            role.setDeletedBy(deleter);

            roleRepository.save(role);

            return ResponseEntity.ok("Role deleted successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete role");
        }
    }



    private RoleDto mapToDto(Role role) {
        Long userCount = roleRepository.countUsersByRoleId(role.getId());

        return modelMapper.map(role, RoleDto.class);
//        return RoleDto.builder()
//                .id(role.getId())
//                .name(role.getName())
//                .description(role.getDescription())
//                .type(role.getType())
//                .userCount(userCount.intValue()) // Set user count
//                .createdAt(role.getCreatedAt())
//                .build();
    }

}
