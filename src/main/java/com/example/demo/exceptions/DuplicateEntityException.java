package com.example.demo.exceptions;

public class DuplicateEntityException extends RuntimeException {
    public DuplicateEntityException(String type, String attribute) {
        super(String.format("%s with name %s already exists.", type, attribute));
    }
    public DuplicateEntityException(String message) {
        super(message);
    }
}
