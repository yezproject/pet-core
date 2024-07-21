package org.yezproject.pet.authentication.application.api_token;

import org.yezproject.pet.domain_common.UseCase;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@UseCase
class ApiTokenGeneratorService {
    private final MessageDigest digest;

    public ApiTokenGeneratorService() {
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

    String generate(String userId, String tokenId) {
        String combination = userId + "." + tokenId;
        byte[] hash = digest.digest(combination.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return "yzp_%s".formatted(hexString.toString());
    }
}
