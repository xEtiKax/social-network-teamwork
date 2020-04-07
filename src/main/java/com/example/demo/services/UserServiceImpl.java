package com.example.demo.services;

import com.example.demo.exceptions.DuplicateEntityException;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    public List<User> getAll() {
        List<User> users = new ArrayList<>(userRepository.findAll());
        if (users.isEmpty()) {
            throw new EntityNotFoundException("There are no registered users");
        }
        return users;
    }

    @Override
    public User createUser(User user) {
        if (checkUserExist(user.getUsername())) {
            throw new DuplicateEntityException("User with this username already exist");
        }
        if (checkEmailExist(user.getEmail())) {
            throw new DuplicateEntityException("User with this email already exist");
        }
        userRepository.save(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!checkUserExist(user.getUsername())) {
            throw new EntityNotFoundException("User %s does not exist");
        }
        userRepository.save(user);
        return user;
    }


    @Override
    public void deleteUser(int id) {
        if (!checkUserExistById(id)) {
            throw new EntityNotFoundException("User %s does not exist");
        }
        userRepository.deleteUser(id);
    }

    private boolean checkUserExist(String username) {
        User user = userRepository.getUserByUsername(username);
        return user != null;
    }

    private boolean checkEmailExist(String email) {
        User user = userRepository.getUserByEmail(email);
        return user != null;
    }

    private boolean checkUserExistById(int id) {
        User user = userRepository.getById(id);
        return user != null;
    }

}
