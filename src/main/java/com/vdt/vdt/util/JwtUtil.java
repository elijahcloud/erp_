package com.vdt.vdt.util;

import com.vdt.vdt.entity.User;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.vdt.vdt.repository.UserRepository;
import javax.crypto.spec.SecretKeySpec;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.security.Key;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@Component
public class JwtUtil {

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    private final String SECRET_KEY = System.getenv("JWT_SECRET_KEY");
    @Autowired
    private UserRepository userRepository;

    private final Key SIGNING_KEY;

    public JwtUtil() {
        if (SECRET_KEY == null || SECRET_KEY.isEmpty()) {
            throw new IllegalStateException("JWT_SECRET_KEY environment variable is not set or is empty.");
        }
        this.SIGNING_KEY = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS512.getJcaName());
    }

    private final long EXPIRATION_TIME = 30 * 60 * 1000; // 30 minutes
    private final Set<String> tokenBlacklist = new HashSet<>();

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("roles", user.getRoles().stream()
                        .map(role -> role.getRole().getName()) // Include only role names
                        .toList())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    public void invalidateToken(String token) {
        tokenBlacklist.add(token); // Add the token to the blacklist
    }

    public boolean isTokenInvalidated(String token) {
        return tokenBlacklist.contains(token);
    }

    public String generateResetToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail()); // Add email to claims
        return createToken(claims, user.getEmail()); // Generate token with claims and subject
    }

    public String generatePreLoginToken() {
        return Jwts.builder()
                .setSubject("pre-login")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    public User validateResetToken(String token) {
        try {
            Claims claims = extractAllClaims(token); // Extract claims from the token
            String email = claims.get("email", String.class); // Retrieve email from claims
            if (email == null) {
                throw new IllegalArgumentException("Email not found in token claims");
            }
            Optional<User> userOpt = userRepository.findByEmail(email);
            return userOpt.orElse(null);
        } catch (Exception e) {
            System.out.println("Invalid reset token: " + e.getMessage());
            return null;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long extractUserId(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return Long.valueOf(claims.getSubject()); // Assuming the user ID is stored as the subject
        } catch (Exception e) {
            System.out.println("Failed to extract user ID from token: " + e.getMessage());
            return null;
        }
    }


    public boolean validateToken(String token) {
        try {
            System.out.println("Validating token: " + token);
            Jwts.parserBuilder().setSigningKey(SIGNING_KEY).build().parseClaimsJws(token);
            System.out.println("Token validation successful");
            return true;
        } catch (io.jsonwebtoken.security.SignatureException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token expired: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
        }
        return false;
    }
}
