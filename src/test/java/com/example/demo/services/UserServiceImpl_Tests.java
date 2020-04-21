package com.example.demo.services;

import com.example.demo.exceptions.DuplicateEntityException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.exceptions.WrongPasswordException;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.Factory.createUser;
import static com.example.demo.Factory.createUserDTO;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserServiceImpl_Tests {

    @Mock
    UserRepository mockUserRepository;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl mockUserService;

    @Test
    public void getAllUsersShould_CallRepository() {
        //Arrange
        List<User> users = new ArrayList<>();
        users.add(createUser());
        Mockito.when(mockUserRepository.findAll()).thenReturn(users);

        //Act
        mockUserService.getAll();

        //Assert
        Mockito.verify(mockUserRepository,
                times(1)).findAll();
    }

    @Test(expected = DuplicateEntityException.class)
    public void createUserShouldThrow_When_UserNameAlreadyExists() throws IOException {
        //Arrange
        User user = createUser();
        Mockito.when(mockUserRepository.getUserByUsername(anyString()))
                .thenReturn(user);

        //Act
        mockUserService.createUser(createUserDTO());
    }

    @Test(expected = DuplicateEntityException.class)
    public void createUserShouldThrow_When_UserEmailAlreadyExists() throws IOException {
        //Arrange
        User user = createUser();
        Mockito.when(mockUserRepository.getUserByEmail(anyString()))
                .thenReturn(user);

        //Act
        mockUserService.createUser(createUserDTO());
    }

    @Test(expected = EntityNotFoundException.class)
    public void getAllUsersShould_Throw_When_NoUsers() {
        //Assert
        Assert.assertTrue(mockUserService.getAll().isEmpty());
    }

    @Test
    public void getAllUsersShould_ReturnAllUsers() {

        List<User> users = new ArrayList<>();
        users.add(createUser());
        Mockito.when(mockUserRepository.findAll()).thenReturn(users);

        List<User> actual = mockUserService.getAll();

        Assert.assertEquals(users, actual);

    }

    @Test(expected = EntityNotFoundException.class)
    public void getUserByIdShouldThrow_WhenUserDoesNotExist() {
        mockUserService.getById(anyLong());
    }

    @Test
    public void getUserByUsernameShould_CallRepository() {
        User user = createUser();
        Mockito.when(mockUserRepository.getUserByUsername(user.getUsername())).thenReturn(user);

        mockUserService.getByUsername(user.getUsername());

        Mockito.verify(mockUserRepository, times(2)).getUserByUsername(user.getUsername());

    }

    @Test(expected = EntityNotFoundException.class)
    public void getUserByUsernameShouldThrow_WhenUserDoesNotExist() {
        mockUserService.getByUsername(anyString());
    }

    @Test
    public void getUserByUsernameShould_ReturnUser_When_UserExists() {
        User user = createUser();
        Mockito.when(mockUserRepository.getUserByUsername(anyString())).thenReturn(user);

        User returnedUser = mockUserService.getByUsername(anyString());

        Assert.assertSame(user, returnedUser);

    }

    @Test
    public void updateUser_Should_CallRepository() {
        Mockito.when(mockUserRepository.existsById(anyLong())).thenReturn(true);

        mockUserService.updateUser(createUser());

        Mockito.verify(mockUserRepository, times(1)).save(any(User.class));

    }

    @Test(expected = EntityNotFoundException.class)
    public void updateUserShouldThrow_WhenUserDoesNotExist() {
        mockUserService.updateUser(createUser());
    }

    @Test
    public void updateUserShould_ReturnUpdatedUser() {
        User user = createUser();
        Mockito.when(mockUserRepository.existsById(anyLong())).thenReturn(true);
        Mockito.when(mockUserRepository.save(any(User.class))).thenReturn(user);

        User returnedUser = mockUserService.updateUser(user);

        Assert.assertSame(user, returnedUser);
    }

    @Test
    public void updateUserDetailsShould_CallRepository() {
        User user = createUser();
        Mockito.when(mockUserRepository.existsById(anyLong())).thenReturn(true);
        Mockito.when(mockUserRepository.save(user)).thenReturn(user);

        mockUserService.updateUser(user);

        Mockito.verify(mockUserRepository, times(1)).save(any(User.class));

    }

    @Test(expected = EntityNotFoundException.class)
    public void updateUserDetailsShould_ThrowIfUserDoesNotExist() {
        mockUserService.updateUserDetails(createUser(), "username", "email", "email",25,"developer");

    }

    @Test
    public void updateUserDetailsShould_UpdateUserDetails() {
        User expectedUser = createUser();
        Mockito.when(mockUserRepository.save(any(User.class))).thenReturn(expectedUser);
        Mockito.when(mockUserRepository.existsById(anyLong())).thenReturn(true);

        mockUserService.updateUserDetails(createUser(), "username", "email", "email",25,"developer");

        Assert.assertSame(expectedUser, expectedUser);
    }

    @Test
    public void addProfilePicture_Should_CallRepository() {
        User user = createUser();
        when(mockUserRepository.existsById(anyLong())).thenReturn(true);
        when(mockUserService.updateUser(user)).thenReturn(user);
        MultipartFile file = new MultipartFile() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getOriginalFilename() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

            }
        };
        Mockito.when(mockUserRepository.getUserByUsername(user.getUsername())).thenReturn(user);
        Mockito.when(mockUserService.getByUsername(user.getUsername())).thenReturn(user);

        mockUserService.addProfilePicture(user.getUsername(), file);

        Mockito.verify(mockUserRepository, times(1)).save(user);

    }

    @Test
    public void changeUserPasswordShould_CallRepository() {
        User user = createUser();
        Mockito.when(passwordEncoder.matches("pass", "pass"));

        mockUserService.changePassword(user.getUsername(), "pass", "pass","pass");
        Mockito.verify(mockUserRepository, times(1)).save(any(User.class));
    }

    @Test(expected = WrongPasswordException.class)
    public void changeUserPasswordShould_Throw_WhenPasswordDoesntMatch() {
        mockUserService.changePassword(createUser().getUsername(), "pass", "p","asd");
    }

}
