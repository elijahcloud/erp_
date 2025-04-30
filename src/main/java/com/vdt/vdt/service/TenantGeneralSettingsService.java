package com.vdt.vdt.service;

import com.vdt.vdt.dto.TenantGeneralSettingsDTO;
import com.vdt.vdt.entity.Tenant;
import com.vdt.vdt.entity.TenantSetting;
import com.vdt.vdt.repository.TenantRepository;
import com.vdt.vdt.repository.TenantSettingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class TenantGeneralSettingsService {

    private final TenantRepository tenantRepository;
    private final TenantSettingRepository tenantSettingRepository;

    @Transactional
    public String saveGeneralSettings(TenantGeneralSettingsDTO dto, MultipartFile logoFile) {
        if (dto.getTenantId() == null) {
            throw new RuntimeException("Tenant ID is required.");
        }

        // Find existing Tenant
        Tenant tenant = tenantRepository.findById(dto.getTenantId())
            .orElseThrow(() -> new RuntimeException("Tenant not found with ID: " + dto.getTenantId()));

        // Validate if the domain ends with "vdt.com"
        if (!dto.getTenantDomain().endsWith("vdt.com")) {
            throw new RuntimeException("Invalid domain. The domain must end with 'vdt.com'.");
        }

        // Check if the domain already exists for a different tenant
        boolean domainExists = tenantRepository.existsByCodeAndIdNot(dto.getTenantDomain(), tenant.getId());
        if (domainExists) {
            throw new RuntimeException("Tenant domain already exists. Please choose a different domain.");
        }

        // ========= NEW: HANDLE LOGO FILE ============
        if (logoFile != null && !logoFile.isEmpty()) {
            String originalFilename = logoFile.getOriginalFilename();
            if (originalFilename == null) {
                throw new RuntimeException("Original filename is missing.");
            }
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();

            if (!extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("png")) {
                throw new RuntimeException("Only JPG, JPEG, and PNG files are allowed.");
            }

            String mimeType = logoFile.getContentType();
            if (mimeType == null || 
                (!mimeType.equals("image/jpeg") && !mimeType.equals("image/png"))) {
                throw new RuntimeException("Invalid image type.");
            }

            String newFileName = UUID.randomUUID().toString() + "." + extension;
            Path logoDir = Paths.get("src/main/resources/static/uploads/logo");
            Path targetPath = logoDir.resolve(newFileName);

            try {
                Files.createDirectories(logoDir);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create upload directory.", e);
            }

            int attempts = 0;
            boolean success = false;

            while (attempts < 3 && !success) {
                try (InputStream in = logoFile.getInputStream()) {
                    Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);

                    try (InputStream imageCheck = Files.newInputStream(targetPath)) {
                        if (ImageIO.read(imageCheck) == null) {
                            throw new IOException("File is not a valid image.");
                        }
                    }

                    success = true;
                } catch (IOException e) {
                    attempts++;
                    if (attempts >= 3) {
                        try {
                            Files.deleteIfExists(targetPath);
                        } catch (IOException ex) {
                            System.err.println("Failed to delete invalid file: " + ex.getMessage());
                        }
                        throw new RuntimeException("Failed to save logo file after 3 attempts.", e);
                    }
                }
            }

            tenant.setLogo(newFileName); // ✅ Save the final name into the database
        }

        // ========= EXISTING TENANT FIELD UPDATES ============
        tenant.setName(dto.getOrganizationName());
        tenant.setCode(dto.getTenantDomain());
        tenantRepository.save(tenant);

        // ========= EXISTING TENANT SETTINGS LOGIC ============
        TenantSetting tenantSetting = tenantSettingRepository.findByTenantSettingTenantId(tenant.getId());
        if (tenantSetting == null) {
            throw new RuntimeException("Settings not found for tenant ID: " + tenant.getId());
        }

        tenantSetting.setTenantSettingMinPasswordLength(Integer.parseInt(dto.getPasswordMinLength()));
        tenantSetting.setTenantSettingRequireSpecialCharacters(Boolean.TRUE.equals(dto.getPasswordRequireSpecial()));
        tenantSetting.setTenantSettingRequireNumbers(Boolean.TRUE.equals(dto.getPasswordRequireNumbers()));
        tenantSetting.setTenantSettingRequireUppercaseLetters(Boolean.TRUE.equals(dto.getPasswordRequireUppercase()));
        tenantSetting.setTenantSettingSessionTimeoutMinutes(Integer.parseInt(dto.getSessionTimeout()));
        tenantSetting.setTenantSettingRequireMfa(Boolean.TRUE.equals(dto.getMfaEnabled()));
        tenantSetting.setTenantSettingDarkModeEnabled(Boolean.TRUE.equals(dto.getDarkMode()));
        tenantSettingRepository.save(tenantSetting);

        return "Tenant and Settings saved successfully.";
    }


    @Transactional
    public TenantGeneralSettingsDTO fetchTenantInfo(Long tenantId) {
        if (tenantId == null) {
            throw new RuntimeException("Tenant ID is required.");
        }

        // Fetch Tenant
        Tenant tenant = tenantRepository.findById(tenantId).orElse(null);

        // Fetch TenantSetting
        TenantSetting tenantSetting = tenantSettingRepository.findByTenantSettingTenantId(tenantId);

        // Build DTO to return
        TenantGeneralSettingsDTO dto = new TenantGeneralSettingsDTO();

        // Populate fields from Tenant
        dto.setOrganizationName(tenant != null ? tenant.getName() : null);
        dto.setAddress(tenant != null ? tenant.getAddress() : null);
        dto.setTenantLogo(tenant != null ? tenant.getLogo() : null); // Add tenant_logo

        // Populate fields from TenantSetting
        dto.setPasswordMinLength(tenantSetting != null ? String.valueOf(tenantSetting.getTenantSettingMinPasswordLength()) : null);
        dto.setPasswordRequireSpecial(tenantSetting != null ? tenantSetting.getTenantSettingRequireSpecialCharacters() : null);
        dto.setPasswordRequireNumbers(tenantSetting != null ? tenantSetting.getTenantSettingRequireNumbers() : null);
        dto.setPasswordRequireUppercase(tenantSetting != null ? tenantSetting.getTenantSettingRequireUppercaseLetters() : null);
        dto.setSessionTimeout(tenantSetting != null ? String.valueOf(tenantSetting.getTenantSettingSessionTimeoutMinutes()) : null);
        dto.setMfaEnabled(tenantSetting != null ? tenantSetting.getTenantSettingRequireMfa() : null);
        dto.setDarkMode(tenantSetting != null ? tenantSetting.getTenantSettingDarkModeEnabled() : null);

        // Fetch tenant_domain_name using TenantRepository
        String tenantDomainName = tenant != null 
            ? tenantRepository.findTenantDomainNameByTenantId(tenant.getId()).orElse(null) 
            : null;
        dto.setTenantDomain(tenantDomainName);

        return dto;
    }
}
