package com.vdt.vdt.controller;

import com.vdt.vdt.dto.ServiceSubscriptionRequestDto;
import com.vdt.vdt.service.SubscriptionServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SubscriptionServiceController {

    @Autowired
    SubscriptionServiceService subscriptionServiceService;

    @PostMapping("/api/subscription")
    public ResponseEntity<?> addServiceSubscriptionForCustomer(@RequestParam Long customerId, @RequestBody ServiceSubscriptionRequestDto serviceSubscriptionRequestDto){
        System.out.println("....................."+customerId);
        return ResponseEntity.ok(subscriptionServiceService.addServiceSubscription(customerId,serviceSubscriptionRequestDto));
    }

    @GetMapping("/api/subscription")
    public ResponseEntity<?> getCustomerServices(@RequestParam Long customerId){
        return ResponseEntity.ok(subscriptionServiceService.getCustomerServiceSubscription(customerId));
    }

}
