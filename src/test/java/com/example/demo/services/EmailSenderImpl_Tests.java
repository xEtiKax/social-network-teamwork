package com.example.demo.services;

import com.example.demo.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailSenderImpl_Tests {

    @Mock
    JavaMailSender javaMailSender;

    @InjectMocks
    EmailSenderServiceImpl emailSenderService;

    @Test
    public void sendInformationEmail_Should_Call_SendEmail() {
        User user = new User();
        user.setEmail("user@email.com");

        emailSenderService.sendInformationEmail(user,"subject","text");
        verify(javaMailSender,times(1)).send(any(SimpleMailMessage.class));
    }
}
