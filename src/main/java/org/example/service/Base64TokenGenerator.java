package org.example.service;

import org.springframework.util.Base64Utils;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

public class Base64TokenGenerator implements TokenGenerator {

    private static final int TOKEN_LENGTH = 6;
    private static final int CAPACITY = 4;
    private final AtomicInteger next = new AtomicInteger(0);

    @Override
    public String generate() {
        byte[] array = ByteBuffer.allocate(CAPACITY).putInt(next.incrementAndGet()).array();
        return Base64Utils.encodeToUrlSafeString(array).substring(0, TOKEN_LENGTH);
    }
}
