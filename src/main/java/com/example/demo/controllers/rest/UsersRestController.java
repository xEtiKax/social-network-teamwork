package com.example.demo.controllers.rest;

import com.example.demo.exceptions.DuplicateEntityException;
import com.example.demo.models.DTO.UserDTO;
import com.example.demo.models.User;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersRestController {

    private UserService userService;

    @Autowired
    public UsersRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id){
        try{
            return userService.getById(id);
        }catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
    }

    @GetMapping("/getByUsername/{username}")
    public User getUserByUsername(@PathVariable String username){
        try{
            return userService.getByUsername(username);
        }catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
    }


    @GetMapping("/getAll")
    public List<User>getAllUsers() {
        try{
            return userService.getAll();
        }catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
    }
    @PostMapping("/new")
    public User createUser(@RequestBody UserDTO userDTO) {
        try {
            userService.createUser(userDTO);
        }catch (DuplicateEntityException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT,e.getMessage());
        }
        return userService.getByUsername(userDTO.getUsername());
    }

    @PutMapping("/update/{id}")
    public User updateUser(@RequestBody UserDTO userDTO, @PathVariable long id) {
        User user = userService.getById(id);
        try {
            user.setUsername(userDTO.getUsername());
            userService.updateUser(user);
        }catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return user;
    }
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable long id) {
        try{
            userService.deleteUser(id);
        }catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
    }
}
