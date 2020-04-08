package com.example.demo.controllers.rest;

import com.example.demo.exceptions.DuplicateEntityException;
import com.example.demo.models.Like;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.services.LikeService;
import com.example.demo.services.PostService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;

@RestController
public class LikeRestController {

    private UserService userService;
    private PostService postService;
    private LikeService likeService;

    @Autowired
    public LikeRestController(UserService userService, PostService postService, LikeService likeService) {
        this.userService = userService;
        this.postService = postService;
        this.likeService = likeService;
    }

    @PostMapping("/like/{postId}")
    public void likePost(@PathVariable int postId, @RequestHeader String userWhoLike) {
        User user = userService.getByUsername(userWhoLike);
        Post post = postService.getPostById(postId);
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);

        try {
            likeService.createLike(like);
//            post.getPostLikers().add(user);
        } catch (DuplicateEntityException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You already likes this post");
        }
    }

    @DeleteMapping("/dislike/{postId}")
    public void dislikePost(@PathVariable int postId, @RequestHeader String userWhoDisLike) {
        Post post = postService.getPostById(postId);
        User user = userService.getByUsername(userWhoDisLike);
//        post.getPostLikers().removeIf(p -> p.getId() == postId);
        Like like = likeService.getLikeByUserId(user.getId());
        try {
            likeService.deleteLike(like);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You don't like this post");
        }
    }
}
