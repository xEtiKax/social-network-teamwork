package com.example.demo.services;

import com.example.demo.models.DTO.UserDTO;
import com.example.demo.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    User getById(int id);

    User getByUsername(String username);

    List<User> getAll();

    List<User> getUserFriends(int userId);

    User createUser(UserDTO userDTO);

    User updateUser(User user);

    void deleteUser(int id);

    void changePassword(String username, String oldPassword, String newPassword);

    void updateUserDetails(User user, String email, String jobTitle);

    void addProfilePicture(String username, MultipartFile profilePicture);
}
