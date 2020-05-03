package com.example.demo.controllers.thymeleaf;

import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.exceptions.DuplicateEntityException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.DTO.CommentDTO;
import com.example.demo.models.DTO.PostDTO;
import com.example.demo.models.User;
import com.example.demo.services.interfaces.PostService;
import com.example.demo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    //index page
    @GetMapping("/new")
    public String showNewPostForm(Model model, Principal principal) {
        model.addAttribute("post", new PostDTO());
        try {
            User user = userService.getByUsername(principal.getName());
            model.addAttribute("user", user);
            model.addAttribute("friendsCounter", user.getFriends().size());
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        }
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

    @GetMapping("/edit/{id}")
    public String showEditPostForm(@PathVariable long id, Model model, Principal principal) {
        try {
            User user = userService.getByUsername(principal.getName());
            model.addAttribute("user", user);
            model.addAttribute("friendsCounter", user.getFriends().size());
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        }
        model.addAttribute("post", postService.getPostById(id));
        return "index";
    }

    @PostMapping("/update/{id}")
    public String updatePost(@PathVariable long id, @ModelAttribute PostDTO postDTO, Model model) {
        try {
            postService.updatePost(id, postDTO);
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        }
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable long id, Model model, Principal principal, HttpServletRequest request) {
        try {
            User user = userService.getByUsername(principal.getName());
            postService.deletePost(id, user, request.isUserInRole("ROLE_ADMIN"));
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        } catch (AuthorizationException auth) {
            model.addAttribute("auth", auth);
        }
        return "redirect:/";
    }

    //myProfile
    @PostMapping("/user/add")
    public String addUserPost(@Valid @ModelAttribute("post") PostDTO postDTO, Model model, Principal principal) {
        try {
            User createdBy = userService.getByUsername(principal.getName());
            postService.createPost(postDTO, createdBy.getId());
        } catch (DuplicateEntityException e) {
            model.addAttribute("error", e);
            return "error";
        }
        return "redirect:/user/showMyProfile";
    }

    @GetMapping("/user/edit/{id}")
    public String showUserEditPostForm(@PathVariable long id, Model model, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("friendsCounter", user.getFriends().size());
        model.addAttribute("post", postService.getPostById(id));
        return "my-profile-feed";
    }

    @PostMapping("/user/update/{id}")
    public String updateUserPost(@PathVariable long id, @ModelAttribute PostDTO postDTO, Model model) {
        try {
            postService.updatePost(id, postDTO);
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        }
        return "redirect:/user/showMyProfile";
    }

    @PostMapping("user/delete/{id}")
    public String deleteUserPost(@PathVariable long id, Model model, Principal principal, HttpServletRequest request) {
        try {
            User user = userService.getByUsername(principal.getName());
            postService.deletePost(id, user, request.isUserInRole("ROLE_ADMIN"));
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        } catch (AuthorizationException auth) {
            model.addAttribute("auth", auth);
        }
        return "redirect:/user/showMyProfile";
    }

    // userProfile
    @GetMapping("/userProfile/edit/{postId}/{userProfileId}")
    public String showUserProfileEditPostForm(@PathVariable long postId, @PathVariable long userProfileId, Model model, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("userProfile", userService.getById(userProfileId));
        model.addAttribute("friendsCounter", user.getFriends().size());
        model.addAttribute("post", postService.getPostById(postId));
        model.addAttribute("comment", new CommentDTO());
        model.addAttribute("isFriend", true);
        model.addAttribute("isFriendAndIsSentRequest", false);
        model.addAttribute("isVisiblePicture", true);
        model.addAttribute("isInfo", false);
        return "user-profile";
    }

    @PostMapping("/userProfile/update/{postId}/{userProfileId}")
    public String updateUserProfilePost(@PathVariable long postId, @PathVariable long userProfileId, @ModelAttribute PostDTO postDTO, Model model) {
        try {
            postService.updatePost(postId, postDTO);
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        }
        return "redirect:/user/showUserProfile/" + userProfileId;
    }

    @PostMapping("/userProfile/delete/{postId}/{userProfileId}")
    public String deleteUserProfilePost(@PathVariable long postId, @PathVariable long userProfileId, Model model, Principal principal, HttpServletRequest request) {
        try {
            User user = userService.getByUsername(principal.getName());
            postService.deletePost(postId, user, request.isUserInRole("ROLE_ADMIN"));
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        } catch (AuthorizationException auth) {
            model.addAttribute("auth", auth);
        }
        return "redirect:/user/showUserProfile/" + userProfileId;
    }


}
