package com.example.demo.controllers.rest;

import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.DTO.PostDTO;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.services.interfaces.PostService;
import com.example.demo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {
    private PostService postService;
    private UserService userService;

    @Autowired
    public PostRestController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable long id) {
        return postService.getPostById(id);
    }

    @PostMapping("/create")
    public void createPost(@RequestBody PostDTO postDTO) {
        try {
            postService.createPost(postDTO, postDTO.getCreatedBy());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/update/{id}")
    public void updatePost(@PathVariable long id, @RequestBody PostDTO postDTO) {
        postService.updatePost(id, postDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePost(@PathVariable long id, Principal principal, HttpServletRequest request) {
        try {
            User user = userService.getByUsername(principal.getName());
            postService.deletePost(id, user, request.isUserInRole("ROLE_ADMIN"));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException auth) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, auth.getMessage());
        }
    }
}
