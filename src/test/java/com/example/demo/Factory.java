package com.example.demo;

import com.example.demo.models.DTO.UserDTO;
import com.example.demo.models.User;

public class Factory {
    public static User createUser() {
        User user = new User();
        user.setUsername("user");
        user.setJobTitle("Developer");
        user.setAge(30);
        user.setPassword("pass");
        return user;
    }

    public static UserDTO createUserDTO() {
        UserDTO user = new UserDTO();
        user.setUsername("user");
        user.setPassword("pass");
        user.setPasswordConfirmation("pass");
        return user;
    }
}