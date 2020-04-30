package com.example.demo.controllers.thymeleaf;

import com.example.demo.exceptions.DuplicateEntityException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.Like;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.services.interfaces.LikeService;
import com.example.demo.services.interfaces.PostService;
import com.example.demo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class LikeController {

    private UserService userService;
    private PostService postService;
    private LikeService likeService;

    @Autowired
    public LikeController(UserService userService, PostService postService, LikeService likeService) {
        this.userService = userService;
        this.postService = postService;
        this.likeService = likeService;
    }

    @RequestMapping("/like/{postId}")
    public String likePost(@PathVariable long postId, Principal principal) {
        try {
            User user = userService.getByUsername(principal.getName());
            Post post = postService.getPostById(postId);
            likeService.createLike(user, post);
        } catch (DuplicateEntityException ignored) {
        }
        return "redirect:/user/showMyProfile";
    }

    @RequestMapping("/dislike/{postId}")
    public String dislikePost(@PathVariable long postId, Principal principal) {
        try {
            Post post = postService.getPostById(postId);
            User user = userService.getByUsername(principal.getName());
            Like like = likeService.getLikeByUserIdAndPostId(user.getId(), postId);
            likeService.deleteLike(like, post, user);
        } catch (EntityNotFoundException ignored) {
        }
        return "redirect:/user/showMyProfile";
    }


    @RequestMapping("/likePublicPost/{postId}")
    public String likePublicPost(@PathVariable long postId, Principal principal) {
        try {
            User user = userService.getByUsername(principal.getName());
            Post post = postService.getPostById(postId);
            likeService.createLike(user, post);
        } catch (DuplicateEntityException ignored) {
        }
        return "redirect:/";
    }

    @RequestMapping("/dislikePublicPost/{postId}")
    public String dislikePublicPost(@PathVariable long postId, Principal principal) {
        try {
            Post post = postService.getPostById(postId);
            User user = userService.getByUsername(principal.getName());
            Like like = likeService.getLikeByUserIdAndPostId(user.getId(), postId);
            likeService.deleteLike(like, post, user);
        }catch (EntityNotFoundException ignored) {
        }
        return "redirect:/";
    }
    @RequestMapping("/likeProfilePost/{postId}")
    public String likeProfilePost(@PathVariable long postId, Principal principal) {
        long creatorId = 0;
        try {
            User user = userService.getByUsername(principal.getName());
            Post post = postService.getPostById(postId);
            creatorId = post.getCreatedBy().getId();
            likeService.createLike(user, post);
        } catch (DuplicateEntityException ignored) {
        }
        return "redirect:/user/showUserProfile/" + creatorId;
    }

    @RequestMapping("/dislikeProfilePost/{postId}")
    public String dislikeProfilePost(@PathVariable long postId, Principal principal) {
        long creatorId = 0;
        try {
            Post post = postService.getPostById(postId);
            User user = userService.getByUsername(principal.getName());
            Like like = likeService.getLikeByUserIdAndPostId(user.getId(), postId);
            creatorId = post.getCreatedBy().getId();
            likeService.deleteLike(like, post, user);
        }catch (EntityNotFoundException ignored) {
        }
        return "redirect:/user/showUserProfile/" + creatorId;
    }
}

