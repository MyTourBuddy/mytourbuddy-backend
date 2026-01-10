package com.mytourbuddy.backend.util;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class IdGenerator {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int ID_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

    public String generate(String prefix, ExistsChecker checker) {
        String id;
        int maxAttempts = 10;
        int attempts = 0;

        do {
            StringBuilder sb = new StringBuilder(prefix);
            for (int i = 0; i < ID_LENGTH; i++) {
                int index = random.nextInt(CHARACTERS.length());
                sb.append(CHARACTERS.charAt(index));
            }
            id = sb.toString();
            attempts++;

            if (attempts >= maxAttempts) {
                throw new RuntimeException("Failed to generate unique ID after " + maxAttempts + " attempts");
            }
        } while (checker.exists(id));

        return id;
    }

    @FunctionalInterface
    public interface ExistsChecker {
        boolean exists(String id);
    }
}
