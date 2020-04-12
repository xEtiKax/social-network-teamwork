package com.example.demo.exceptions;

public class WrongEmailException extends RuntimeException {
    public WrongEmailException(String message) {
        super(message);
    }
}
