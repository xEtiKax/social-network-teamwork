package com.example.demo.services;

import com.example.demo.exceptions.DuplicateEntityException;
import com.example.demo.exceptions.WrongEmailException;
import com.example.demo.exceptions.WrongPasswordException;
import com.example.demo.models.DTO.UserDTO;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.demo.utils.Mapper.userDTOtoUserMapper;

@Service
public class UserServiceImpl implements UserService {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
    public List<User> getUserFriends(int userId) {
        return userRepository.getUserFriendsByUSerId(userId);
    }

    @Override
    public User createUser(UserDTO userDTO) {
        User user = userDTOtoUserMapper(userDTO);
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
    public void updateUserDetails(User user, String email, String jobTitle) {
        if (!validate(email)) {
            throw new WrongEmailException("Wrong email format");
        } else {
            user.setEmail(email);
            user.setJobTitle(jobTitle);
            userRepository.save(user);
        }
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        User user = getByUsername(username);
        boolean passMatched = passwordEncoder.matches(oldPassword, user.getPassword());
        if (passMatched) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new WrongPasswordException("Wrong old password");
        }
    }

    @Override
    public void addProfilePicture(String username, MultipartFile profilePicture) {
        User user = getByUsername(username);
        try {
            user.setPhoto(multiPartToByteArr(profilePicture));
        } catch (IOException e) {
            e.printStackTrace();
        }
        userRepository.save(user);
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

    public Byte[] multiPartToByteArr(@RequestParam("imageFile") MultipartFile file) throws IOException {
        Byte[] byteObjects = new Byte[file.getBytes().length];
        int i = 0;
        for (byte b : file.getBytes()) {
            byteObjects[i++] = b;
        }
        return byteObjects;
    }

    public static boolean validate(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }


}
