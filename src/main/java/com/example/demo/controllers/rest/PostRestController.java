package com.example.demo.controllers.rest;

import com.example.demo.models.Post;
import com.example.demo.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public void createPost(@RequestBody Post post, Principal principal) {
        postService.createPost(post);
    }

    @PostMapping("/update/{id}")
    public void updatePost(@PathVariable int id, @RequestBody Post post, Principal principal) {
        postService.updatePost(id, post);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePost(@PathVariable int id){
        postService.deletePost(id);
    }
}
