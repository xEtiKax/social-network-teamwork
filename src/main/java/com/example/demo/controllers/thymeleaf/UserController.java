package com.example.demo.controllers.thymeleaf;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.PostService;
import com.example.demo.services.UserService;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

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
}
