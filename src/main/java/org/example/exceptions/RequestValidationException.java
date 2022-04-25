package org.example.exceptions;

import java.util.HashMap;
import java.util.Map;

public class RequestValidationException extends RuntimeException {

    private final Map<String, String> errors = new HashMap<>();

    public RequestValidationException(Map<String, String> errors) {
        this.errors.putAll(errors);
    }

    public RequestValidationException(String message){
        super(message);
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
