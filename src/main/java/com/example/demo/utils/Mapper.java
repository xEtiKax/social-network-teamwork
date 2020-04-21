package com.example.demo.utils;

import com.example.demo.models.DTO.UserDTO;
import com.example.demo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Mapper {

    @Autowired
    public Mapper() {

    }

    public static User userDTOtoUserMapper(UserDTO userDTO) throws IOException {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEnabled(1);
        user.setPassword(userDTO.getPassword());
        return user;
    }

}
