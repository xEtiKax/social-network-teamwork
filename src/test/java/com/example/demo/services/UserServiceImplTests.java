package com.example.demo.services;

import com.example.demo.exceptions.DuplicateEntityException;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.Factory.createUser;
import static com.example.demo.Factory.createUserDTO;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
public class UserServiceImplTests {

    @Mock
    UserRepository mockUserRepository;

//    @Mock
//    PostService mockPostService;
//
//    @Mock
//    BCryptPasswordEncoder passwordEncoder;

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
    public void createUserShouldThrow_When_UserNameAlreadyExists() {
        //Arrange
        User user = createUser();
        Mockito.when(mockUserRepository.getUserByUsername(anyString()))
                .thenReturn(user);

        //Act
        mockUserService.createUser(createUserDTO());
    }
}
