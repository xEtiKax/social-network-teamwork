package com.example.demo.services;

import com.example.demo.exceptions.DuplicateEntityException;
import com.example.demo.exceptions.WrongEmailException;
import com.example.demo.exceptions.WrongPasswordException;
import com.example.demo.models.DTO.UserDTO;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.interfaces.UserService;
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
    public User getById(long id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    public List<User> getAll() {
        return new ArrayList<>(userRepository.findAll());
    }

    @Override
    public List<User> getByNameLikeThis(String username) {
        return userRepository.findAllByUsernameIsContaining(username);
    }

    @Override
    public List<User> getUserFriends(long userId) {
        return userRepository.getUserFriendsByUSerId(userId);
    }

    @Override
    public void createUser(UserDTO userDTO) throws IOException {
        User user = userDTOtoUserMapper(userDTO);
        if (checkUserExist(user.getUsername())) {
            throw new DuplicateEntityException("User with this username already exist");
        }
        if (checkEmailExist(user.getEmail())) {
            throw new DuplicateEntityException("User with this email already exist");
        }
        userRepository.save(user);
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
    public void updateUserDetails(User user, String firstName, String lastName, String email, int age, String jobTitle) {
        if (!validate(email)) {
            throw new WrongEmailException("Wrong email format");
        } else {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setAge(age);
            user.setJobTitle(jobTitle);
            userRepository.save(user);
        }
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword, String passwordConfirm) {
        if (!newPassword.equals(passwordConfirm)) {
            throw new WrongPasswordException("Passwords does not match");
        }
        User user = getByUsername(username);
        boolean oldPasswordMatched = passwordEncoder.matches(oldPassword, user.getPassword());
        if (oldPasswordMatched) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new WrongPasswordException("Wrong password");
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
    public void removeFriend(User me, User friend) {
        me.removeFriend(friend);
        userRepository.save(me);
    }

    @Override
    public void addCoverPhoto(String username, MultipartFile coverPhoto) {
        User user = getByUsername(username);
        try {
            user.setCoverPhoto(multiPartToByteArr(coverPhoto));
        } catch (IOException e) {
            e.printStackTrace();
        }
        userRepository.save(user);
    }

    @Override
    public void deleteUser(long id) {
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

    private boolean checkUserExistById(long id) {
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
