package com.example.demo.controllers.thymeleaf;

import com.example.demo.exceptions.DuplicateEntityException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.exceptions.WrongPasswordException;
import com.example.demo.models.DTO.UserDTO;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.PostService;
import com.example.demo.services.UserService;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

import static com.example.demo.utils.Mapper.userDTOtoUserMapper;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private UserRepository userRepository;
    private PostService postService;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, PostService postService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.postService = postService;
    }

    @GetMapping
    public String showuserProfile(Model model, Principal principal) {
        model.addAttribute("user", userService.getByUsername(principal.getName()));
        return "user";
    }

    @GetMapping("/showMyPosts")
    public String showMyPosts(Model model, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        model.addAttribute("myPosts", postService.getPostsByUserId(user.getId()));
        return "myPosts";
    }

    @GetMapping("/showMyFriends")
    public String showFriends(Model model, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        List<User> friends = userService.getUserFriends(user.getId());
        model.addAttribute("friends", friends);
        return "friends";
    }

    @GetMapping("/new")
    public String showNewUserForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "user";
    }

    @PostMapping("/new")
    public String createUser(@Valid @ModelAttribute("user") UserDTO userDTO, Model model, BindingResult error) {
        if (error.hasErrors()) {
            return "user";
        }
        try {
            userService.createUser(userDTO);
        } catch (DuplicateEntityException e) {
            model.addAttribute("error", e);
            return "error";
        }
        return "redirect:/user";
    }

    @GetMapping("/changePass")
    public String changeUserPassword(Model model, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        model.addAttribute("user", user);
        return "changePass";
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public String changePassword(@RequestParam(name = "oldPassword") String oldPassword,
                                 @RequestParam(name = "newPassword") String newPassword,
                                 Principal principal, Model model) {
        User user = userService.getByUsername(principal.getName());
        model.addAttribute("user", user);
        try {
            userService.changePassword(user.getUsername(), oldPassword, newPassword);
        } catch (WrongPasswordException e) {
            model.addAttribute("error", "Wrong password");
            return "changePass";
        }
        model.addAttribute("success", "Password changes successful");
        return "changePass";
    }

    @GetMapping("details/{userId}")
    public String showDetails(Model model, @PathVariable int userId) {
        User user = userService.getById(userId);
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping(value = "/user/edit")
    public String editUserDetails(Model model,
                                  Principal principal) {
        User user = userService.getByUsername(principal.getName());
        model.addAttribute("user", user);
        return "editUser";
    }

    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public String updateUserProfile(@RequestParam("firstName") String firstName,
                                    @RequestParam("lastName") String lastName,
                                    @RequestParam("email") String email,
                                    Principal principal,
                                    Model model) {
        User user = userService.getByUsername(principal.getName());
        try {
            userService.updateUserDetails(user, firstName, lastName, email);
            model.addAttribute("success", "Profile updated successfully!");
        } catch (DuplicateEntityException e) {
            model.addAttribute("error", "Email already exists");
        }
        model.addAttribute("user", user);
        return "editUser";
    }

    @RequestMapping(value = "/changeProfilePicture", method = RequestMethod.POST)
    public String changeProfilePicture(Principal principal, Model model,
                                       @RequestParam("profilePicture") MultipartFile profilePicture) {
        try {
            User user = userService.getByUsername(principal.getName());
            userService.addProfilePicture(user.getUsername(), profilePicture);
            model.addAttribute("success", "Picture changed successful");
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/user";
    }
}
