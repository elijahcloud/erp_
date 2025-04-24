package com.vdt.vdt.service;

import com.vdt.vdt.repository.TenantRepository;
import com.vdt.vdt.util.JwtUtil; // Import JwtUtil for token validation
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TenantService {

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private JwtUtil jwtUtil; // Add JwtUtil for token validation

    public long getActiveTenantCount(String authorizationHeader) {
        System.out.println("Received authorization header: " + authorizationHeader); // Debugging

        // Validate the authorization header
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }

        String token = authorizationHeader.substring(7); // Extract the token
        System.out.println("Extracted token: " + token); // Debugging

        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Invalid or expired token");
        }

        // Fetch and return the count of active tenants excluding those with tenant_code as VDT001
        long activeTenantCount = tenantRepository.countByIsActiveTrueAndCodeNot("VDT001");
        System.out.println("Active tenant count: " + activeTenantCount); // Debugging
        return activeTenantCount;
    }
}
