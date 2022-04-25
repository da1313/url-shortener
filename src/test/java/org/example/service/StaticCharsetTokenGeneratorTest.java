package org.example.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StaticCharsetTokenGeneratorTest {

    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    public static final int TOKEN_LENGTH = 6;

    @Test
    void shouldGenerateRandomSequenceFromHardcodedSet() {
        TokenGenerator tokenGenerator = new StaticCharsetTokenGenerator();
        String actual = tokenGenerator.generate();
        Assertions.assertEquals(TOKEN_LENGTH, actual.length());
        for (int i = 0; i < actual.length(); i++) {
            Assertions.assertTrue(CHARS.contains(String.valueOf(actual.charAt(i))));
        }
    }
}