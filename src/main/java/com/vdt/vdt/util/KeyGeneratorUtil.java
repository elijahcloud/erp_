package com.vdt.vdt.util;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGeneratorUtil {

    public static String generateSecretKey(int length) {
        if (length < 32) {
            throw new IllegalArgumentException("Key length must be at least 32 bytes for security.");
        }
        byte[] key = new byte[length];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    public static void main(String[] args) {
        // Example: Generate a 64-byte secret key
        String secretKey = generateSecretKey(64);
        System.out.println("Generated Secret Key: " + secretKey);
    }
}
