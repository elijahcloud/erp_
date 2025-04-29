package com.vdt.vdt.controller;

import com.vdt.vdt.entity.KYCStatus;
import com.vdt.vdt.service.KYCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class KYCController {
    @Autowired
    KYCService kycService;

    //To update KYC of customer with either status or document
    @PatchMapping("api/customer/kyc")
    ResponseEntity<?> updateKYC(
            @RequestParam Long customerId,
            @RequestParam(required = false) MultipartFile document,
            @RequestParam(required = false) KYCStatus status
    ) throws IOException {
        return ResponseEntity.ok(kycService.updateKYCDocument(customerId,document, status));
    }
}
