package com.example.demo.controllers.thymeleaf;

import com.example.demo.models.DTO.CommentDTO;
import com.example.demo.models.DTO.RequestDTO;
import com.example.demo.models.Like;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.services.interfaces.LikeService;
import com.example.demo.services.interfaces.PostService;
import com.example.demo.services.interfaces.RequestService;
import com.example.demo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {
    PostService postService;
    UserService userService;
    LikeService likeService;
    RequestService requestService;

    @Autowired
    public HomeController(PostService postService,
                          UserService userService,
                          LikeService likeService,
                          RequestService requestService) {
        this.postService = postService;
        this.userService = userService;
        this.likeService = likeService;
        this.requestService = requestService;
    }

    @GetMapping("/")
    public String showHomePage(Model model, Principal principal) {
        model.addAttribute("posts", postService.getAllPublicPosts());
        int friendsCounter = 0;
        try {
            User user = userService.getByUsername(principal.getName());
            friendsCounter = user.getFriends().size();
            checkLike(user);
            model.addAttribute("user", user);
        } catch (Exception ignored) {
        }
        model.addAttribute("friendsCounter", friendsCounter);
        model.addAttribute("users", userService.getAll());
        model.addAttribute("comment", new CommentDTO());
        model.addAttribute("request", new RequestDTO());

        return "index";
    }

    private void checkLike(User user) {
        List<Post> posts = postService.getAllPublicPosts();
        for (Post post : posts) {
            for (Like like : post.getLikes()) {
                if (like.getUser().getId() == user.getId()) {
                    post.setLiked(true);
                }
            }
        }
    }
}