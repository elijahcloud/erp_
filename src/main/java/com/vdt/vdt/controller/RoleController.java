package com.vdt.vdt.controller;

import com.vdt.vdt.dto.RoleDto;
import com.vdt.vdt.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/roles") // Updated route to conform with AuthController

public class RoleController {

    private final RoleService roleService;

    private RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<?> getAllRoles(@RequestHeader(value = "Authorization", required = true) String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authorization header is required");
        }
        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid Authorization header format");
        }
        try {
            return roleService.getAllRoles(authorizationHeader);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch roles", ex);
        }
    }

    @PostMapping("/add-role")
    public ResponseEntity<?> addRole(
            @RequestBody RoleDto roleDto,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            return roleService.addRole(roleDto, authorizationHeader);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add role", ex);
        }
    }

    @PostMapping("/edit-role")
    public ResponseEntity<?> editRole(@RequestBody RoleDto roleDto,
                                      @RequestHeader("Authorization") String authorizationHeader) {
        try {
            return roleService.editRole(roleDto, authorizationHeader);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to edit role", ex);
        }
    }

    @DeleteMapping("/delete-role/{id}")
    public ResponseEntity<?> deleteRole(
            @PathVariable UUID id,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            return roleService.deleteRole(id, authorizationHeader);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete role", ex);
        }
    }
}
