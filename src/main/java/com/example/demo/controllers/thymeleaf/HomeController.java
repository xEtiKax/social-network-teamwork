package com.example.demo.controllers.thymeleaf;

import com.example.demo.models.User;
import com.example.demo.services.interfaces.PostService;
import com.example.demo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {
    PostService postService;
    UserService userService;

    @Autowired
    public HomeController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String showHomePage(Model model, Principal principal) {
        model.addAttribute("posts", postService.getAllPublicPosts());
        try {
            User user = userService.getByUsername(principal.getName());
            model.addAttribute("user", user);
        }catch (Exception e){
            System.out.println(e);
        }
        return "index";
    }

    @GetMapping("/admin")
    public String showAdminPage() {
        return "admin";
    }
}