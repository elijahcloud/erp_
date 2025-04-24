package com.vdt.vdt.controller;

import com.vdt.vdt.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/roles") // Updated route to conform with AuthController
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<?> getAllRoles(@RequestHeader(value = "Authorization", required = true) String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            System.err.println("WARNING: Missing Authorization header in getAllRoles request."); // Debug log added
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authorization header is required");
        }
        System.out.println("DEBUG: Received request to fetch all roles.");
        System.out.println("Authorization Header: " + authorizationHeader); // Debug log added
        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid Authorization header format");
        }
        try {
            return roleService.getAllRoles(authorizationHeader);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch roles", e);
        }
    }
}
