package com.example.demo.services.interfaces;

public interface ForgottenPasswordService {
    boolean userValidation(String username, String email);

    void sendEmailForPassword(String username);

}

