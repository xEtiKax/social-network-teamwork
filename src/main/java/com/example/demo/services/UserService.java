package com.example.demo.services;

import com.example.demo.models.User;

import java.util.List;

public interface UserService {

    User getById(int id);

    User getByUsername(String username);

    List<User> getAll();

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(int id);

}
