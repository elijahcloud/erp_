package com.vdt.vdt.controller;

import com.vdt.vdt.dto.*;
import com.vdt.vdt.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import com.vdt.vdt.util.JwtUtil; // Import the JwtUtil class
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.vdt.vdt.dto.TokenResponse; // Update to the correct package if TokenResponse is in 'dto'

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtUtil jwtUtil; // Add JwtUtil as a dependency

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, @RequestHeader(value = "Authorization", required = true) String authorizationHeader) {
        try {
            return authService.login(loginRequest, authorizationHeader);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login failed", e);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        try {
            return authService.logout(token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Logout failed", e);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
        @Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest,
        @RequestHeader(value = "Authorization", required = true) String authorizationHeader
    ) {
        try {
            return authService.forgotPassword(forgotPasswordRequest, authorizationHeader); 
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Forgot password request failed", e);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        try {
            System.out.println("Reset password request received: " + resetPasswordRequest);
            return authService.resetPassword(resetPasswordRequest);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reset password failed", e);
        }
    }

    @PostMapping("/switch-role")
    public ResponseEntity<?> switchRole(@Valid @RequestBody SwitchRoleRequest switchRoleRequest) {
        try {
            return authService.switchRole(switchRoleRequest);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Switch role failed", e);
        }
    }

    @GetMapping("/pre-login-token")
    public ResponseEntity<?> getPreLoginToken() {
        try {
            String preLoginToken = jwtUtil.generatePreLoginToken(); // Generate a temporary token
            return ResponseEntity.ok(new TokenResponse(preLoginToken));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate pre-login token", e);
        }
    }
}