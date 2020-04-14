package com.example.demo.controllers.thymeleaf;

import com.example.demo.exceptions.DuplicateEntityException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.DTO.PostDTO;
import com.example.demo.models.User;
import com.example.demo.services.PostService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/post")
public class PostController {
    PostService postService;
    UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/new")
    public String showNewPostForm(Model model) {
        model.addAttribute("post", new PostDTO());
        return "index";
    }

    @PostMapping("/add")
    public String addPost(@Valid @ModelAttribute("post") PostDTO postDTO, Model model, Principal principal) {
        try {
            User createdBy = userService.getByUsername(principal.getName());
            postService.createPost(postDTO, createdBy.getId());
        } catch (DuplicateEntityException e) {
            model.addAttribute("error", e);
            return "error";
        }
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable long id, Model model, Principal principal, HttpServletRequest request) {
        try {
            postService.deletePost(id, principal, request);
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        }
        return "redirect:/";
    }
}
