package org.example.service;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class StaticCharsetTokenGenerator implements TokenGenerator {

    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int TOKEN_LENGTH = 6;

    @Override
    public String generate() {
        Random random = new Random();
        int baseLength = CHARS.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            int code = random.nextInt(baseLength);
            sb.append(CHARS.charAt(code));
        }
        return sb.toString();
    }
}
