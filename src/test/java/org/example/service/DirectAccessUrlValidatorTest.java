package org.example.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DirectAccessUrlValidatorTest {

    public static final String BROKEN_URL = "google.ru";
    public static final String ULI_WITH_MALFORMED_SCHEMA = "httpX://google.ru";
    public static final String VALID_URI = "http://google.ru";
    public static final int TIMEOUT = 3000;

    JavaNetURLPingService validator = new JavaNetURLPingService();

    @Test
    void shouldAcceptNullValues() {
        boolean actual = validator.pingURL(null, TIMEOUT);
        Assertions.assertFalse(actual);
    }

    @Test
    void shouldFailOnNoSchemaInURL() {
        boolean actual = validator.pingURL(BROKEN_URL, TIMEOUT);
        Assertions.assertFalse(actual);
    }

    @Test
    void shouldFailOnUnsupportedSchema(){
        boolean actual = validator.pingURL(ULI_WITH_MALFORMED_SCHEMA, TIMEOUT);
        Assertions.assertFalse(actual);
    }

    @Test
    void shouldSucceedOnValidUrl(){
        boolean actual = validator.pingURL(VALID_URI, TIMEOUT);
        Assertions.assertTrue(actual);
    }

}