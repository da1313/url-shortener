package org.example.exceptions;

public class TokenGenerationException extends RuntimeException {
    public TokenGenerationException(String message){
        super(message);
    }
}
