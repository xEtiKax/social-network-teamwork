package com.example.demo.services;

import com.example.demo.exceptions.DuplicateEntityException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.exceptions.WrongEmailException;
import com.example.demo.exceptions.WrongPasswordException;
import com.example.demo.models.DTO.UserDTO;
import com.example.demo.models.Picture;
import com.example.demo.models.User;
import com.example.demo.repositories.PictureRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

    public static final String USERNAME_ALREADY_EXIST = "User with this username already exist";
    public static final String USER_DOES_NOT_EXIST = "User does not exist";
    public static final String WRONG_EMAIL_FORMAT = "Wrong email format";
    public static final String Е_MAIL_ALREADY_EXIST = "Е-mail already exist";
    public static final String Е_MAIL_DOESNT_EXIST = "Е-mail does not exist";
    public static final String PASSWORDS_DOESNT_MATCH = "Passwords does not match";
    public static final String WRONG_PASSWORD = "Wrong password";

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private PictureRepository pictureRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, PictureRepository pictureRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.pictureRepository = pictureRepository;
    }


    @Override
    public User getById(long id) {
        throwIfUserDoesNotExists(id);
        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        throwIfUsernameDoesNotExist(username);
        return userRepository.getUserByUsername(username);
    }

    @Override
    public User getByEmail(String email) {
        throwIfEmailDoesNotExists(email);
        return userRepository.findUserByEmail(email);
    }

    @Override
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
    public User createUser(UserDTO userDTO) {
        User user = userDTOtoUserMapper(userDTO);
        if (checkUserExist(user.getUsername())) {
            throw new DuplicateEntityException(USERNAME_ALREADY_EXIST);
        }
        userRepository.save(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        throwIfUserDoesNotExists(user.getId());
        userRepository.save(user);
        return user;
    }

    @Override
    public void updateUserDetails(User user, String firstName, String lastName, String email, int age, String jobTitle) {
        throwIfUserDoesNotExists(user.getId());
        throwIfEmailAlreadyExists(email);
        if (!validate(email)) {
            throw new WrongEmailException(WRONG_EMAIL_FORMAT);
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
            throw new WrongPasswordException(PASSWORDS_DOESNT_MATCH);
        }
        User user = getByUsername(username);
        boolean oldPasswordMatched = passwordEncoder.matches(oldPassword, user.getPassword());
        if (oldPasswordMatched) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } else {
            throw new WrongPasswordException(WRONG_PASSWORD);
        }
    }

    @Override
    public void addProfilePicture(String username, MultipartFile profilePicture) throws IOException {
        User user = getByUsername(username);
        Picture picture = new Picture();
        picture.setData(multiPartToByteArr(profilePicture));
        user.setPhoto(picture);
        pictureRepository.save(picture);
        userRepository.save(user);
    }

    @Override
    public void removeFriend(User me, User friend) {
        me.removeFriend(friend);
        userRepository.save(me);
    }

    @Override
    public void addCoverPhoto(String username, MultipartFile coverPhoto) throws IOException {
        User user = getByUsername(username);
        user.setCoverPhoto(multiPartToByteArr(coverPhoto));
        userRepository.save(user);
    }

    @Override
    public void deleteUser(long id) {
        throwIfUserDoesNotExists(id);
        userRepository.deleteUser(id);
    }

    private boolean checkUserExist(String username) {
        User user = userRepository.getUserByUsername(username);
        return user != null;
    }

    private boolean checkEmailExist(String email) {
        User user = userRepository.findUserByEmail(email);
        return user != null;
    }

    private boolean checkUserExistById(long id) {
        return userRepository.existsById(id);
    }

    private void throwIfUserDoesNotExists(long id) {
        if (!checkUserExistById(id)) {
            throw new EntityNotFoundException(USER_DOES_NOT_EXIST);
        }
    }

    private void throwIfEmailAlreadyExists(String email) {
        if (checkEmailExist(email)) {
            throw new DuplicateEntityException(Е_MAIL_ALREADY_EXIST);
        }
    }

    private void throwIfEmailDoesNotExists(String email) {
        if (!checkEmailExist(email)) {
            throw new EntityNotFoundException(Е_MAIL_DOESNT_EXIST);
        }
    }

    private void throwIfUsernameDoesNotExist(String username) {
        if (!checkUserExist(username)) {
            throw new EntityNotFoundException(USER_DOES_NOT_EXIST);
        }
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
