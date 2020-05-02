package com.example.demo.services;

import com.example.demo.exceptions.DuplicateEntityException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.exceptions.WrongEmailException;
import com.example.demo.exceptions.WrongPasswordException;
import com.example.demo.models.DTO.UserDTO;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.repositories.PictureRepository;
import com.example.demo.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.Factory.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImpl_Tests {

    @Mock
    UserRepository mockUserRepository;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @Mock
    PictureRepository mockPictureRepository;

    @InjectMocks
    UserServiceImpl mockUserService;

    @Test
    public void getAllUsersShould_CallRepository() {

        List<User> users = new ArrayList<>();
        users.add(createUser());
        Mockito.when(mockUserRepository.findAll()).thenReturn(users);


        mockUserService.getAll();


        Mockito.verify(mockUserRepository,
                times(1)).findAll();
    }
    @Test
    public void getByNameLikeShould_CallRepository() {

        User user = createUser();
        List<User> users = new ArrayList<>();
        users.add(user);
        Mockito.when(mockUserRepository.findAllByUsernameIsContaining(anyString())).thenReturn(users);


        mockUserService.getByNameLikeThis(createUser().getUsername());


        Mockito.verify(mockUserRepository,
                times(1)).findAllByUsernameIsContaining(anyString());
    }

    @Test
    public void getUserFriendsShould_CallRepository() {

        User user = createUser();
        List<User> friends = new ArrayList<>();
        friends.add(user);
        Mockito.when(mockUserRepository.getUserFriendsByUSerId(anyLong())).thenReturn(friends);


        mockUserService.getUserFriends(createUser().getId());


        Mockito.verify(mockUserRepository,
                times(1)).getUserFriendsByUSerId(anyLong());
    }

    @Test(expected = DuplicateEntityException.class)
    public void createUserShouldThrow_When_UserNameAlreadyExists() {

        User user = createUser();
        Mockito.when(mockUserRepository.getUserByUsername(anyString()))
                .thenReturn(user);

        mockUserService.createUser(createUserDTO());
    }

    @Test
    public void getAllUsersShould_ReturnAllUsers() {

        List<User> users = new ArrayList<>();
        users.add(createUser());
        Mockito.when(mockUserRepository.findAll()).thenReturn(users);

        List<User> actual = mockUserService.getAll();

        Assert.assertEquals(users, actual);

    }

    @Test
    public void checkLike_Should_checkLike() {
        User user = createUser();
        Post post = createPost();
        post.addLike(createLike());
        List<Post> posts = new ArrayList<>();
        posts.add(post);

        List<Post> actual = mockUserService.checkLike(user,posts);

        Assert.assertEquals(posts, actual);

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


    @Test
    public void getUserIdShould_ReturnUser_When_UserExists() {
        User user = createUser();
        Mockito.when(mockUserRepository.getById(user.getId())).thenReturn(user);
        Mockito.when(mockUserRepository.existsById(user.getId())).thenReturn(true);

        User returnedUser = mockUserService.getById(user.getId());

        Assert.assertSame(user, returnedUser);

    }

    @Test(expected = EntityNotFoundException.class)
    public void getUserByUsernameShouldThrow_WhenUserDoesNotExist() {
        mockUserService.getByUsername(anyString());
    }

    @Test(expected = EntityNotFoundException.class)
    public void getUserByEmailShouldThrow_WhenUserDoesNotExist() {
        mockUserService.getByEmail("email@email.com");
    }
    @Test
    public void getEmailShould_ReturnUser_When_UserEmailExists() {
        User user = createUser();
        Mockito.when(mockUserRepository.findUserByEmail("user@email.com")).thenReturn(user);

        User returnedUser = mockUserService.getByEmail("user@email.com");

        Assert.assertSame(user, returnedUser);

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
        User user = createUser();
        Mockito.when(mockUserRepository.existsById(anyLong())).thenReturn(true);

        mockUserService.updateUser(user);

        Mockito.verify(mockUserRepository, times(1)).save(any(User.class));

    }

    @Test
    public void createUser_Should_CallRepository() {
        UserDTO userDTO = createUserDTO();

        mockUserService.createUser(userDTO);

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
        Mockito.when(mockUserRepository.existsById(anyLong())).thenReturn(true);

        mockUserService.updateUserDetails(createUser(),"Ivan","Ivanov","ivan@mail.bg",25,"Dev");

        Mockito.verify(mockUserRepository, times(1)).save(any(User.class));

    }

    @Test(expected = EntityNotFoundException.class)
    public void updateUserDetailsShould_ThrowIfUserDoesNotExist() {
        mockUserService.updateUserDetails(createUser(), "username", "email", "email@email.com", 25, "developer");

    }

    @Test(expected = DuplicateEntityException.class)
    public void updateUserDetailsShould_Throw_WhenEmailAlreadyExist() {
        User expectedUser = createUser();
        expectedUser.setEmail("user@email.com");
        Mockito.when(mockUserRepository.existsById(anyLong())).thenReturn(true);
        Mockito.when(mockUserRepository.findUserByEmail(anyString())).thenReturn(expectedUser);

        mockUserService.updateUserDetails(expectedUser, "firstName", "lastName", "user@email.com", 25, "developer");

    }

    @Test
    public void updateUserDetailsShould_UpdateUserDetails() {
        User expectedUser = createUser();
        Mockito.when(mockUserRepository.save(any(User.class))).thenReturn(expectedUser);
        Mockito.when(mockUserRepository.existsById(anyLong())).thenReturn(true);

        mockUserService.updateUserDetails(createUser(), "username", "email", "email@email.com", 25, "developer");

        Assert.assertSame(expectedUser, expectedUser);
    }

    @Test(expected = WrongEmailException.class)
    public void updateUserDetailsShouldThrow_WhenWrongEmailFormat() {
        User user = createUser();
        Mockito.when(mockUserRepository.existsById(anyLong())).thenReturn(true);

        mockUserService.updateUserDetails(user, "username", "email", "email", 25, "developer");

    }

    @Test
    public void addProfilePicture_Should_CallRepository() throws IOException {
        User user = createUser();
        when(mockUserRepository.existsById(anyLong())).thenReturn(true);
        when(mockUserService.updateUser(user)).thenReturn(user);
        MultipartFile file = new MultipartFile() {
            @Override
            public String getName() {
                return "fileName";
            }

            @Override
            public String getOriginalFilename() {
                return "originalName";
            }

            @Override
            public String getContentType() {
                return "jpg";
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
                return "Picture bytes".getBytes();
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
    public void addCoverPhoto_Should_CallRepository() throws IOException {
        User user = createUser();
        when(mockUserRepository.existsById(anyLong())).thenReturn(true);
        when(mockUserService.updateUser(user)).thenReturn(user);

        MultipartFile file = new MultipartFile() {
            @Override
            public String getName() {
                return "fileName";
            }

            @Override
            public String getOriginalFilename() {
                return "originalName";
            }

            @Override
            public String getContentType() {
                return "jpg";
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
                return "Picture bytes".getBytes();
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

        mockUserService.addCoverPhoto(user.getUsername(), file);

        Mockito.verify(mockUserRepository, times(1)).save(user);

    }

    @Test
    public void changeUserPasswordShould_CallRepository() {
        User user = createUser();
        Mockito.when(mockUserRepository.getUserByUsername(user.getUsername())).thenReturn(user);
        Mockito.when(passwordEncoder.matches("pass", "pass")).thenReturn(true);

        mockUserService.changePassword(user.getUsername(), "pass", "pass", "pass");
        Mockito.verify(mockUserRepository, times(1)).save(any(User.class));
    }

    @Test(expected = WrongPasswordException.class)
    public void changeUserPasswordShouldThrow_WhenPassDontMatch() {
        User user = createUser();
        user.setPassword("parola");
        Mockito.when(mockUserRepository.getUserByUsername(user.getUsername())).thenReturn(user);

        mockUserService.changePassword(user.getUsername(), "pass", "pass", "pass");

    }

    @Test(expected = WrongPasswordException.class)
    public void changeUserPasswordShould_Throw_WhenPasswordDoesntMatch() {
        mockUserService.changePassword(createUser().getUsername(), "p", "pkom", "asd");
    }

    @Test
    public void deleteUserShould_CallRepository() {
        Mockito.when(mockUserRepository.existsById(anyLong())).thenReturn(true);

        mockUserService.deleteUser(anyLong());

        Mockito.verify(mockUserRepository,times(1)).deleteUser(anyLong());
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteUser_ShouldThrow_WhenUserDoesNotExist() {
        mockUserService.deleteUser(anyLong());
    }

    @Test
    public void removeFriend_ShouldRemoveFriend() {

        User user = createUser();
        User user2 = createUser();
        User user3 = createUser();
        user.addFriend(user2);
        user.addFriend(user3);

        mockUserService.removeFriend(user,user2);

        Assert.assertEquals(user.getFriends().size(),1);
    }
}
