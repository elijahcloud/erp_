package com.vdt.vdt.controller;

import com.vdt.vdt.dto.*;
import com.vdt.vdt.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Endpoints for authentication-related operations")
@Validated
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtUtil jwtUtil; // Add JwtUtil as a dependency

    @Operation(summary = "Login", description = "Authenticate the user and return a JWT token.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, @RequestHeader(value = "Authorization", required = true) String authorizationHeader) {
        try {
            return authService.login(loginRequest, authorizationHeader);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login failed", e);
        }
    }

    @Operation(summary = "Logout", description = "Logs out the user and invalidates the JWT token.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logout successful"),
        @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        try {
            return authService.logout(token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Logout failed", e);
        }
    }

    @Operation(summary = "Forgot Password", description = "Sends an email to reset the user's password.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Email sent successfully"),
        @ApiResponse(responseCode = "400", description = "Bad request")
    })
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

    @Operation(summary = "Reset Password", description = "Resets the user's password based on the token provided.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password reset successful"),
        @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        try {
            System.out.println("Reset password request received: " + resetPasswordRequest);
            return authService.resetPassword(resetPasswordRequest);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reset password failed", e);
        }
    }

    @Operation(summary = "Switch Role", description = "Switches the user's current role.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Role switched successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping("/switch-role")
    public ResponseEntity<?> switchRole(@Valid @RequestBody SwitchRoleRequest switchRoleRequest) {
        try {
            return authService.switchRole(switchRoleRequest);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Switch role failed", e);
        }
    }

    @Operation(summary = "Get Pre-login Token", description = "Generates a temporary pre-login token.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token generated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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