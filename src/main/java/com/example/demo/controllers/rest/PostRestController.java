package com.example.demo.controllers.rest;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.DTO.PostDTO;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.services.PostService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {
    private PostService postService;
    private UserService userService;

    @Autowired
    public PostRestController(PostService postService,UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable int id) {
        return postService.getPostById(id);
    }

    @PostMapping("/create")
    public void createPost(@RequestBody PostDTO postDTO, Principal principal) {
        try {
            User createdBy = userService.getByUsername(principal.getName());
            postService.createPost(postDTO, createdBy);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/update/{id}")
    public void updatePost(@PathVariable int id, @RequestBody PostDTO postDTO, Principal principal) {
        postService.updatePost(id, postDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePost(@PathVariable int id) {
        postService.deletePost(id);
    }
}
