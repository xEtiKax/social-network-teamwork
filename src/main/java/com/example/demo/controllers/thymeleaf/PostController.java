package com.example.demo.controllers.thymeleaf;

import com.example.demo.exceptions.DuplicateEntityException;
import com.example.demo.models.DTO.PostDTO;
import com.example.demo.services.PostService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class PostController {
    PostService postService;
    UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/post/new")
    public String showNewPostForm(Model model) {
        model.addAttribute("post", new PostDTO());
        return "index";
    }

    @PostMapping("/add")
    public String addPost(@Valid @ModelAttribute("post") PostDTO postDTO, BindingResult error, Model model, Principal principal) {
        try {
            postService.createPost(postDTO);
        }catch (DuplicateEntityException e){
            model.addAttribute("error", e);
            return "error";
        }
        return "redirect:/";
    }
}
