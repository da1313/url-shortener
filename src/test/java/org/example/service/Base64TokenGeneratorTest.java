package org.example.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.Base64Utils;

import java.nio.ByteBuffer;

class Base64TokenGeneratorTest {

    public static final int TOKEN_LENGTH = 6;

    @Test
    void shouldGenerateTokenFromBase64EncodedIncrementalIntegerSequence() {
        TokenGenerator tokenGenerator = new Base64TokenGenerator();
        String first = tokenGenerator.generate();
        String second = tokenGenerator.generate();
        Assertions.assertEquals(TOKEN_LENGTH, first.length());
        int base1 = ByteBuffer.allocate(4).put(Base64Utils.decodeFromString(first + "==")).getInt(0);
        int base2 = ByteBuffer.allocate(4).put(Base64Utils.decodeFromString(second + "==")).getInt(0);
        Assertions.assertEquals(1, base1);
        Assertions.assertEquals(2, base2);
    }
}