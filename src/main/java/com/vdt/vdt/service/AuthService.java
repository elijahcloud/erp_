package com.vdt.vdt.service;

import com.vdt.vdt.dto.*;
import com.vdt.vdt.entity.User;
import com.vdt.vdt.repository.UserRepository;
import com.vdt.vdt.util.JwtUtil;
import com.vdt.vdt.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;

    public ResponseEntity<?> login(LoginRequest loginRequest, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            System.out.println("Login request received: " + loginRequest);

            // Extract the token from the Authorization header
            String token = null;
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7); // Remove "Bearer " prefix
            }
            System.out.println("Validating pre-login token: " + token);

            if (token == null || !jwtUtil.validateToken(token)) {
                System.out.println("Invalid pre-login token: " + token);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or expired token");
            }

            Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());
            if (userOpt.isEmpty()) {
                System.out.println("Login failed: User not found for email " + loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            User user = userOpt.get();
            System.out.println("User found: " + user);

            if (!PasswordUtil.verifyPassword(loginRequest.getPassword(), user.getPassword())) {
                System.out.println("Login failed: Incorrect password for email " + loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
            System.out.println("Password validation successful for email " + loginRequest.getEmail());

            String newToken = jwtUtil.generateToken(user);
            System.out.println("Login successful for email " + loginRequest.getEmail() + ". Token: " + newToken);

            // Construct response using LoginResponse
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setId(user.getId().toString()); // Include the user ID
            loginResponse.setEmail(user.getEmail());
            // loginResponse.setName(user.getName()); // Commented out for future use
            loginResponse.setName("John Doe"); // Temporarily set name to "John Doe"
            loginResponse.setRoles(user.getRoles().stream().map(role -> {
                UserRoleResponse roleResponse = new UserRoleResponse();
                roleResponse.setId(role.getId().toString());
                roleResponse.setRole(role.getRole().getName());
                roleResponse.setTenantId(role.getTenant().getId().toString());
                roleResponse.setTenantName(role.getTenant().getName());
                roleResponse.setIsPlatformAdmin(
                    (role.getRole().getName().equalsIgnoreCase("Platform Administrator") || 
                    role.getRole().getName().equalsIgnoreCase("Platform Support Administrator")) && 
                    role.getTenant().getCode().equalsIgnoreCase("VDT001")
                );
                return roleResponse;
            }).toList());
            loginResponse.setActiveRole(user.getActiveRole() != null ? loginResponse.getRoles().stream()
                .filter(role -> role.getId().equals(user.getActiveRole().getId().toString()))
                .findFirst()
                .orElse(null) : null);
            loginResponse.setIsPlatformAdmin(loginResponse.getRoles().stream().anyMatch(UserRoleResponse::getIsPlatformAdmin));
            loginResponse.setToken(newToken);

            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            System.out.println("Exception during login: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    public ResponseEntity<?> logout(String token) {
        jwtUtil.invalidateToken(token);
        return ResponseEntity.ok("Logged out successfully");
    }

    public ResponseEntity<?> forgotPassword(ForgotPasswordRequest forgotPasswordRequest, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            System.out.println("Forgot password request received: " + forgotPasswordRequest);

            // Extract the token from the Authorization header
            String preLoginToken = null;
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                preLoginToken = authorizationHeader.substring(7); // Remove "Bearer " prefix
            }
            System.out.println("Validating pre-login token: " + preLoginToken);

            if (preLoginToken == null || !jwtUtil.validateToken(preLoginToken)) {
                System.out.println("Invalid pre-login token: " + preLoginToken);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or expired pre-login token");
            }

            Optional<User> userOpt = userRepository.findByEmail(forgotPasswordRequest.getEmail());
            if (userOpt.isEmpty()) {
                System.out.println("Forgot password failed: User not found for email " + forgotPasswordRequest.getEmail());
                return ResponseEntity.badRequest().body("User not found");
            }

            User user = userOpt.get();
            System.out.println("User found: " + user);

            String resetToken = jwtUtil.generateResetToken(user);
            System.out.println("Generated reset token for email " + forgotPasswordRequest.getEmail() + ": " + resetToken);

            try {
                sendResetPasswordEmail(user.getEmail(), resetToken);
                System.out.println("Password reset email sent to: " + user.getEmail());
            } catch (MessagingException e) {
                System.out.println("Failed to send reset email: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send reset email");
            }

            return ResponseEntity.ok("Password reset email sent");
        } catch (Exception e) {
            System.out.println("Exception during forgot password process: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    private void sendResetPasswordEmail(String email, String resetToken) throws MessagingException {
        // Use an environment variable for the reset URL base
        String resetUrlBase = System.getenv("RESET_URL_BASE");
        if (resetUrlBase == null || resetUrlBase.isEmpty()) {
            throw new IllegalStateException("RESET_URL_BASE environment variable is not set");
        }
        // Include both token and email in the reset URL
        String resetUrl = resetUrlBase + "/auth/reset-password?token=" + resetToken + "&email=" + email;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setFrom(System.getenv("EMAIL_USERNAME")); // Ensure the sender matches the authenticated username
        helper.setSubject("Reset Your Password");
        helper.setText(
            "<!DOCTYPE html>" +
            "<html>" +
            "<body>" +
            "<p>Dear User,</p>" +
            "<p>You requested to reset your password. Please click the link below to reset it:</p>" +
            "<p><a href=\"" + resetUrl + "\" style=\"color: #1a73e8; text-decoration: underline; font-weight: bold;\">Reset Your Password</a></p>" +
            "<p>If you did not request this, please ignore this email.</p>" +
            "<p>Thank you,</p>" +
            "<p>Your App Team</p>" +
            "</body>" +
            "</html>",
            true
        );

        int maxRetries = 3;
        int attempt = 0;
        while (attempt < maxRetries) {
            try {
                mailSender.send(message);
                System.out.println("Password reset email sent successfully to: " + email);
                return; // Exit the method if email is sent successfully
            } catch (Exception e) {
                attempt++;
                System.out.println("Attempt " + attempt + " to send email failed: " + e.getMessage());
                if (attempt >= maxRetries) {
                    throw new MessagingException("Failed to send email after " + maxRetries + " attempts", e);
                }
            }
        }
    }

    public ResponseEntity<?> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        try {
            System.out.println("Reset password request received: " + resetPasswordRequest);

            // Validate the reset token
            User userFromToken = jwtUtil.validateResetToken(resetPasswordRequest.getToken());
            if (userFromToken == null) {
                System.out.println("Invalid or expired reset token: " + resetPasswordRequest.getToken());
                return ResponseEntity.badRequest().body("Invalid or expired token");
            }

            // Fetch the user by email
            Optional<User> userOpt = userRepository.findByEmail(resetPasswordRequest.getEmail());
            if (userOpt.isEmpty()) {
                System.out.println("User not found for email: " + resetPasswordRequest.getEmail());
                return ResponseEntity.badRequest().body("User not found");
            }

            User user = userOpt.get();

            // Ensure the email matches the token's user
            if (!user.getEmail().equals(userFromToken.getEmail())) {
                System.out.println("Email mismatch: Token belongs to " + userFromToken.getEmail() + ", but received " + resetPasswordRequest.getEmail());
                return ResponseEntity.badRequest().body("Invalid email for the provided token");
            }

            // Hash the new password and update the user
            String hashedPassword = PasswordUtil.hashPassword(resetPasswordRequest.getPassword());
            user.setPassword(hashedPassword);
            userRepository.save(user);

            System.out.println("Password reset successful for user: " + user.getEmail());
            return ResponseEntity.ok("Password reset successful");
        } catch (Exception e) {
            System.out.println("Exception during password reset: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    public ResponseEntity<?> switchRole(SwitchRoleRequest switchRoleRequest) {
        Optional<User> userOpt = userRepository.findById(switchRoleRequest.getUserId());
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        User user = userOpt.get();
        // Validate if the role exists for the user
        if (user.getRoles().stream().noneMatch(role -> role.getId() == Long.parseLong(switchRoleRequest.getRoleId()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role for the user");
        }

        user.setActiveRole(Long.parseLong(switchRoleRequest.getRoleId()));
        userRepository.save(user);
        return ResponseEntity.ok("Role switched successfully");
    }
}