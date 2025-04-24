package com.vdt.vdt.service;

import com.vdt.vdt.dto.RoleDto;
import com.vdt.vdt.entity.Role;
import com.vdt.vdt.repository.RoleRepository;
import com.vdt.vdt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;

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
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            System.out.println("Roles fetched successfully: " + roles);

            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            System.out.println("Exception while fetching roles: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    private RoleDto mapToDto(Role role) {
        Long userCount = roleRepository.countUsersByRoleId(role.getId()); // Fetch user count
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .type(role.getType())
                .userCount(userCount.intValue()) // Set user count
                .createdAt(role.getCreatedAt())
                .build();
    }
}
