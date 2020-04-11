package com.example.demo.controllers.thymeleaf;

import com.example.demo.models.Post;
import com.example.demo.services.CommentService;
import com.example.demo.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    PostService postService;

    @Autowired
    public HomeController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String showHomePage(Model model) {
        model.addAttribute("posts", postService.getAllPublicPosts());
        return "index";
    }

    @GetMapping("/admin")
    public String showAdminPage() {
        return "admin";
    }
}