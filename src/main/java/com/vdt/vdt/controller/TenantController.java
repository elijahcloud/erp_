package com.vdt.vdt.controller;

import com.vdt.vdt.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @GetMapping("/active")
    public ResponseEntity<?> getActiveTenants(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            System.out.println("Received Authorization Header: " + authorizationHeader); // Debugging
            long activeTenantCount = tenantService.getActiveTenantCount(authorizationHeader);
            return ResponseEntity.ok().body(activeTenantCount);
        } catch (Exception e) {
            System.out.println("Error fetching active tenants: " + e.getMessage()); // Debugging
            return ResponseEntity.status(500).body("Failed to fetch active tenants");
        }
    }
}
