package com.vdt.vdt.controller;

import com.vdt.vdt.dto.TenantGeneralSettingsDTO;
import com.vdt.vdt.dto.TenantRequestDTO;
import com.vdt.vdt.service.TenantGeneralSettingsService;
import com.vdt.vdt.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private TenantGeneralSettingsService tenantGeneralSettingsService;

    @GetMapping("/active")
    public ResponseEntity<?> getActiveTenants() {
        try {
            long activeTenantCount = tenantService.getActiveTenantCount();
            return ResponseEntity.ok().body(activeTenantCount);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch active tenants", ex);
        }
    }

    @PostMapping("/create-tenant")
    public ResponseEntity<?> createTenant(@RequestBody TenantRequestDTO request, @RequestHeader("Authorization") String authHeader) {
        try {
            tenantService.createTenant(request, authHeader);
            return ResponseEntity.ok("Tenant created successfully.");
        } catch (Exception ex) {
            HttpStatus status = ex instanceof RuntimeException ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR;
            throw new ResponseStatusException(status, ex.getMessage(), ex);
        }
    }

    @GetMapping("/{tenantId}/general-settings")
    public ResponseEntity<?> getGeneralSettings(@PathVariable Long tenantId) {
        try {
            TenantGeneralSettingsDTO settings = tenantGeneralSettingsService.fetchTenantInfo(tenantId);
            return ResponseEntity.ok(settings);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch tenant general settings", ex);
        }
    }

    @PostMapping("/{tenantId}/general-settings")
    public ResponseEntity<?> saveGeneralSettings(
        @PathVariable Long tenantId,
        @RequestPart("settings") TenantGeneralSettingsDTO settingsDTO,
        @RequestPart(value = "logo", required = false) MultipartFile logoFile) {

        try {
            settingsDTO.setTenantId(tenantId);
            String message = tenantGeneralSettingsService.saveGeneralSettings(settingsDTO, logoFile);
            return ResponseEntity.ok(message);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save tenant general settings", ex);
        }
    }
}
