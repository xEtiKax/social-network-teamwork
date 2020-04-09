package com.example.demo.controllers.rest;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.DTO.PostDTO;
import com.example.demo.models.Post;
import com.example.demo.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {
    private PostService postService;

    @Autowired
    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable int id) {
        return postService.getPostById(id);
    }

    @PostMapping("/create")
    public void createPost(@RequestBody PostDTO postDTO, Principal principal) {
        try {
            postService.createPost(postDTO);
        }
        catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/update/{id}")
    public void updatePost(@PathVariable int id, @RequestBody PostDTO postDTO, Principal principal) {
        postService.updatePost(id, postDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePost(@PathVariable int id){
        postService.deletePost(id);
    }
}
