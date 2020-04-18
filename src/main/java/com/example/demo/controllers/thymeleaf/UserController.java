package com.example.demo.controllers.thymeleaf;

import com.example.demo.exceptions.DuplicateEntityException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.exceptions.WrongEmailException;
import com.example.demo.exceptions.WrongPasswordException;
import com.example.demo.models.Like;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.services.interfaces.LikeService;
import com.example.demo.services.interfaces.PostService;
import com.example.demo.services.interfaces.RequestService;
import com.example.demo.services.interfaces.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private RequestService requestService;
    private PostService postService;
    private PasswordEncoder passwordEncoder;
    private LikeService likeService;

    @Autowired
    public UserController(UserService userService, PostService postService,
                          PasswordEncoder passwordEncoder, RequestService requestService, LikeService likeService) {
        this.userService = userService;
        this.postService = postService;
        this.passwordEncoder = passwordEncoder;
        this.requestService = requestService;
        this.likeService = likeService;
    }

    @GetMapping("/showUserProfile/{userId}")
    public String showUserProfile(Model model, @PathVariable long userId, Principal principal) {
        User me = userService.getByUsername(principal.getName());
        User user = userService.getById(userId);

        boolean isSentRequest = requestService.checkIfRequestExist(me.getId(), userId);
        List<Post> posts = postService.getPostsByUserId(userId);
        boolean isFriend = false;
        boolean isFriendAndIsSentRequest = true;
        if (!user.getFriends().contains(me) && !isSentRequest) {
            isFriendAndIsSentRequest = false;
        }
        if (user.getFriends().contains(me)) {
            isFriend = true;
        }

        int friendsCounter = user.getFriends().size();

        model.addAttribute("isSentRequest", isSentRequest);
        model.addAttribute("user", user);
        model.addAttribute("isFriend", isFriend);
        model.addAttribute("isFriendAndIsSentRequest", isFriendAndIsSentRequest);
        model.addAttribute("posts", posts);
        model.addAttribute("friendsCounter", friendsCounter);
        return "user-profile";
    }

    @GetMapping("/showMyProfile")
    public String showMyProfile(Model model, Principal principal) {

        User user = userService.getByUsername(principal.getName());
        int friendsCounter = user.getFriends().size();
        List<Post> posts = postService.getPostsByUserId(user.getId());

        for (Post post : posts) {
            for (Like like:post.getLikes()) {
                if (like.getUser().getId() == user.getId()){
                    post.setLiked(true);
                }
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("myPosts", postService.getPostsByUserId(user.getId()));
        model.addAttribute("post", new Post());
        model.addAttribute("friendsCounter", friendsCounter);
        return "my-profile-feed";
    }

    @GetMapping("/showMyFriends")
    public String showFriends(Model model, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        List<User> friends = userService.getUserFriends(user.getId());
        model.addAttribute("friends", friends);
        return "friends";
    }

    @GetMapping("/showAllUsers")
    public String showUsers(Model model) {
        model.addAttribute("users", userService.getAll());
        return "profiles";
    }

    @GetMapping("/changePass")
    public String changeUserPassword(Model model, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        model.addAttribute("user", user);
        return "change-password";
    }

    @GetMapping("/privacy")
    public String showPrivacy(Model model, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        model.addAttribute("user", user);
        return "privacy";
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public String changePassword(@RequestParam(name = "oldPassword") String oldPassword,
                                 @RequestParam(name = "newPassword") String newPassword,
                                 @RequestParam("passwordConfirm") String passwordConfirm,
                                 Principal principal, Model model) {
        try {
            User user = userService.getByUsername(principal.getName());
            userService.changePassword(user.getUsername(), oldPassword, newPassword, passwordConfirm);
            model.addAttribute("success", "Password changes successful");
        } catch (WrongPasswordException e) {
            model.addAttribute("error", "Wrong password");
        }
        return "change-password";
    }

    @GetMapping(value = "/edit")
    public String editUserDetails(Model model,
                                  Principal principal) {
        User user = userService.getByUsername(principal.getName());
        model.addAttribute("user", user);
        return "user-settings";
    }

    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public String updateUserProfile(@RequestParam("firstName") String firstName,
                                    @RequestParam("lastName") String lastName,
                                    @RequestParam("email") String email,
                                    @RequestParam("age") int age,
                                    @RequestParam("jobTitle") String jobTitle,
                                    Principal principal,
                                    Model model) {
        User user = userService.getByUsername(principal.getName());
        try {
            userService.updateUserDetails(user, firstName, lastName, email, age, jobTitle);
            model.addAttribute("success", "Profile updated successfully!");
        } catch (DuplicateEntityException e) {
            model.addAttribute("error", "Email already exists");
        } catch (WrongEmailException e) {
            model.addAttribute("wrongEmail", "Wrong email format");
        }
        model.addAttribute("user", user);
        return "user-settings";
    }

    @GetMapping(value = "/deleteProfile")
    public String deleteUserProfile(Model model,
                                    Principal principal) {
        User user = userService.getByUsername(principal.getName());
        model.addAttribute("user", user);
        return "deactivate-account";
    }

    @RequestMapping(value = "/deleteProfile")
    public String deleteProfile(Principal principal, Model model,
                                @RequestParam("password") String password, HttpServletRequest request) {
        User user = userService.getByUsername(principal.getName());

        for (Post post : postService.getPostsByUserId(user.getId())) {
            postService.deletePost(post.getId(), principal, request);
        }
        boolean passwordMatch = passwordEncoder.matches(password, user.getPassword());
        if (passwordMatch) {
            userService.deleteUser(user.getId());
        } else {
            model.addAttribute("wrongPassword", "Wrong email or password");
            return "deactivate-account";
        }
        return "index";
    }
}
