package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.interfaces.EmailSenderService;
import com.example.demo.services.interfaces.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)

public class ForgottenPasswordImpl_Tests {

    @Mock
    UserService userService;
    @Mock
    UserDetailsManager userDetailsManager;
    @Mock
    UserRepository userRepository;
    @Mock
    JavaMailSender javaMailSender;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    EmailSenderService emailSenderService;
    @InjectMocks
    ForgottenPasswordServiceImpl forgottenPasswordService;

    @Test
    public void userValidation_Should_ReturnTrue_When_EmailAndUsernameIsValid() {
        User user = new User();
        when(userDetailsManager.userExists("user")).thenReturn(true);
        when(userService.getByEmail("user@email.com")).thenReturn(user);

        boolean result = forgottenPasswordService.userValidation("user", "user@email.com");

        Assert.assertTrue(result);
    }

    @Test
    public void userValidation_Should_ReturnFalse_When_EmailNotExist() {
        boolean result = forgottenPasswordService.userValidation("user", "user@email.com");
        Assert.assertFalse(result);
    }

    @Test
    public void userValidation_Should_ReturnFalse_When_UsernameNotExist() {
        when(userDetailsManager.userExists("user")).thenReturn(false);

        boolean result = forgottenPasswordService.userValidation("user", "user@email.com");

        Assert.assertFalse(result);
    }

    @Test
    public void sendEmailForPassword_Should_CallSendInformationEmail() {
        User user = new User();
        user.setUsername("username");

        when(userService.getByUsername("username")).thenReturn(user);

        when(passwordEncoder.encode(anyString())).thenReturn("password");

        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        forgottenPasswordService.sendEmailForPassword("username");

        verify(emailSenderService, times(1)).sendInformationEmail(any(User.class), anyString(), anyString());
    }
}
