package com.example.demo.services.interfaces;

import com.example.demo.models.User;
import org.springframework.mail.SimpleMailMessage;

public interface EmailSenderService {

    void sendInformationEmail(User user, String subject, String text);

    void sendEmail(SimpleMailMessage email);
}
