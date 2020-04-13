package com.example.demo.utils;

import com.example.demo.models.DTO.UserDTO;
import com.example.demo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class Mapper {

    private static PasswordEncoder passwordEncoder;

    @Autowired
    public Mapper(PasswordEncoder passwordEncoder) {
        Mapper.passwordEncoder = passwordEncoder;
    }

    public static User userDTOtoUserMapper(UserDTO userDTO){
        User user = new User();
        user.setUsername(userDTO.getUsername());
//        user.setAge(userDTO.getAge());
        user.setEnabled(1);
        user.setPhoto(null);
        user.setPassword(userDTO.getPassword());
        return user;
    }
}
