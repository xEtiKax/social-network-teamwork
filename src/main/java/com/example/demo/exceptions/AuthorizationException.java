package com.example.demo.exceptions;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException() {
        super("You haven't permissions to make this action");
    }

    public AuthorizationException(String message) {
        super(message);
    }
}
