package com.vdt.vdt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {
    @GetMapping("/api/h")
    public ResponseEntity<?> getHealth(){
        return ResponseEntity.ok("Healthy");

    }}
