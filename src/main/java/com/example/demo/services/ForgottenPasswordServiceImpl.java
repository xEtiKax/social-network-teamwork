package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.interfaces.EmailSenderService;
import com.example.demo.services.interfaces.ForgottenPasswordService;
import com.example.demo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.lang.String.format;

@Service
public class ForgottenPasswordServiceImpl implements ForgottenPasswordService {

    private UserService userService;
    private UserDetailsManager userDetailsManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private EmailSenderService emailSender;

    @Autowired
    public ForgottenPasswordServiceImpl(UserService userService, UserRepository userRepository,
                                        PasswordEncoder passwordEncoder,
                                        EmailSenderService emailSender,
                                        UserDetailsManager userDetailsManager) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsManager = userDetailsManager;
        this.emailSender = emailSender;
    }

    @Override
    public boolean userValidation(String username, String email) {
        if (userDetailsManager.userExists(username)) {
            return userService.getByEmail(email) != null;
        }
        return false;
    }

    @Override
    public void sendEmailForPassword(String username) {
        User user = userService.getByUsername(username);
        String password = changeRandomPassword(user);
        emailSender.sendInformationEmail(user, "Forgotten Password", format("Your new password is: %s", password));
    }

    private String generateNewPassword() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    private String changeRandomPassword(User user) {
        String password = generateNewPassword();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return password;

    }

}
