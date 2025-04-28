package com.vdt.vdt.controller;

import com.vdt.vdt.dto.CustomerBillingDTO;
import com.vdt.vdt.service.CustomerBillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerBillingController {

    @Autowired
    private CustomerBillingService billingService;

    @GetMapping("api/customer/billing/{customerId}")
    public ResponseEntity<?> getBilling(@PathVariable Long customerId) {
        try {
            CustomerBillingDTO dto = billingService.getBillingByCustomerId(customerId);
            return ResponseEntity.ok(dto);
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PatchMapping("/api/customer/billing/{customerId}")
    public ResponseEntity<?> updateBilling(@PathVariable Long customerId, @RequestBody CustomerBillingDTO dto) {
        try {
            CustomerBillingDTO updated = billingService.updateBilling(customerId, dto);
            return ResponseEntity.ok(updated);
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
