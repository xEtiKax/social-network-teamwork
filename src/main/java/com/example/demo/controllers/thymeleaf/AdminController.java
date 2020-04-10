package com.example.demo.controllers.thymeleaf;

import com.example.demo.repositories.UserRepository;
import com.example.demo.services.PostService;
import com.example.demo.services.UserService;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;
    private PostService postService;
    private UserRepository userRepository;

    @Autowired
    public AdminController(UserService userService, PostService postService, UserRepository userRepository) {
        this.userService = userService;
        this.postService = postService;
        this.userRepository = userRepository;
    }

    @GetMapping("/showAllUsers")
    public String showUsers(Model model) {
        model.addAttribute("users",userService.getAll());
        return "users";
    }


}
