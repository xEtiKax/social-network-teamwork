package com.example.demo.services.interfaces;

import com.example.demo.models.DTO.UserDTO;
import com.example.demo.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    User getById(long id);

    User getByUsername(String username);

    List<User> getAll();

    List<User> getUserFriends(long userId);

    List<User> getByNameLikeThis(String username);

    void createUser(UserDTO userDTO) throws IOException;

    User updateUser(User user);

    void deleteUser(long id);

    void changePassword(String username, String oldPassword, String newPassword, String passwordConfirm);

    void updateUserDetails(User user, String firstName, String lastName, String email, int age, String jobTitle);

    void addProfilePicture(String username, MultipartFile profilePicture);

    void addCoverPhoto(String username, MultipartFile profilePicture);

    void removeFriend(User me, User friend);


}
