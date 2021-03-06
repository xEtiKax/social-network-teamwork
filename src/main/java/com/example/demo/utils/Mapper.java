package com.example.demo.utils;

import com.example.demo.models.DTO.UserDTO;
import com.example.demo.models.Like;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    @Autowired
    public Mapper() {

    }

    public static User userDTOtoUserMapper(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEnabled(1);
        user.setPassword(userDTO.getPassword());
        return user;
    }

    public static void likeMapper(User user, Post post) {
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
    }

}
