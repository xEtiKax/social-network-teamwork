package com.example.demo.services.interfaces;

public interface ForgottenPasswordService {
    boolean userValidation(String username, String email);

    void sendEmailForPassword(String username);

//    void sendInformationEmail(User user, String subject, String text);

//    void sendEmail(SimpleMailMessage email);
}

