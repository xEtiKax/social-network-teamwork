package com.example.demo.controllers.thymeleaf;

import com.example.demo.exceptions.DuplicateEntityException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.exceptions.WrongEmailException;
import com.example.demo.exceptions.WrongPasswordException;
import com.example.demo.models.DTO.UserDTO;
import com.example.demo.models.Like;
import com.example.demo.models.Post;
import com.example.demo.models.Request;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.LikeService;
import com.example.demo.services.PostService;
import com.example.demo.services.RequestService;
import com.example.demo.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

import static com.example.demo.utils.Mapper.userDTOtoUserMapper;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private UserRepository userRepository;
    private RequestService requestService;
    private PostService postService;
    private PasswordEncoder passwordEncoder;
    private LikeService likeService;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, PostService postService,
                          PasswordEncoder passwordEncoder, RequestService requestService, LikeService likeService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.postService = postService;
        this.passwordEncoder = passwordEncoder;
        this.requestService = requestService;
        this.likeService = likeService;
    }

    @GetMapping ("/showUserProfile/{userId}")
    public String showUserProfile(Model model,@PathVariable int userId, Principal principal) {
        User me = userService.getByUsername(principal.getName());
        User user = userService.getById(userId);

        List<Post> posts = postService.getPostsByUserId(userId);
        boolean isFriend = false;
        if (user.getFriends().contains(me)){
            isFriend = true;
        }
        model.addAttribute("user",user);
        model.addAttribute("isFriend",isFriend);
        model.addAttribute("posts",posts);
        return "user-profile";
    }

    @GetMapping("/showMyProfile")
    public String showMyProfile(Model model, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        model.addAttribute("user",user);
        model.addAttribute("myPosts", postService.getPostsByUserId(user.getId()));
        return "my-profile-feed";
    }

    @GetMapping("/showMyFriends")
    public String showFriends(Model model, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        List<User> friends = userService.getUserFriends(user.getId());
        model.addAttribute("friends", friends);
        return "friends";
    }

    @GetMapping("/new")
    public String showNewUserForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "user";
    }

    @PostMapping("/new")
    public String createUser(@Valid @ModelAttribute("user") UserDTO userDTO, Model model, BindingResult error) {
        if (error.hasErrors()) {
            return "user";
        }
        try {
            userService.createUser(userDTO);
        } catch (DuplicateEntityException e) {
            model.addAttribute("error", e);
            return "error";
        }
        return "redirect:/user";
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
        model.addAttribute("user",user);
        return "privacy";
    }

    @PostMapping("/like/{postId}/")
    public String likePost(@PathVariable int postId, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        Post post = postService.getPostById(postId);
        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        likeService.createLike(like);
        return "redirect:/post/details" + post.getId();
    }

    @DeleteMapping("/dislike/{postId}")
    public String dislikePost(@PathVariable int postId, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        Like like = likeService.getLikeByUserIdAndPostId(user.getId(), postId);
        likeService.deleteLike(like.getId());
            return "redirect:/post/details";
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public String changePassword(@RequestParam(name = "oldPassword") String oldPassword,
                                 @RequestParam(name = "newPassword") String newPassword,
                                 Principal principal, Model model) {
        User user = userService.getByUsername(principal.getName());
        model.addAttribute("user", user);
        try {
            userService.changePassword(user.getUsername(), oldPassword, newPassword);
        } catch (WrongPasswordException e) {
            model.addAttribute("error", "Wrong password");
            return "change-password";
        }
        model.addAttribute("success", "Password changes successful");
        return "change-password";
    }

    @GetMapping("details/{userId}")
    public String showDetails(Model model, @PathVariable int userId) {
        User user = userService.getById(userId);
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping(value = "/edit")
    public String editUserDetails(Model model,
                                  Principal principal) {
        User user = userService.getByUsername(principal.getName());
        model.addAttribute("user", user);
        return "profile-account-setting";
    }

    @GetMapping(value = "/showRequests")
    public String getFriendRequests(Model model, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        List<Request> friendRequests = requestService.getUserRequests(user.getId());

        for (Request request:friendRequests) {
            User sender = request.getSender();
            model.addAttribute("sender",sender);
        }
        model.addAttribute("requests", friendRequests);
        return "requests";
    }

    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public String updateUserProfile(@RequestParam ("username")String username,@RequestParam("email") String email,
                                    @RequestParam("jobTitle") String jobTitle,
                                    Principal principal,
                                    Model model) {
        User user = userService.getByUsername(principal.getName());
        try {
            userService.updateUserDetails(user,username, email, jobTitle);
            model.addAttribute("success", "Profile updated successfully!");
        } catch (DuplicateEntityException e) {
            model.addAttribute("error", "Email already exists");
        }
        catch (WrongEmailException e) {
            model.addAttribute("wrongEmail", "Wrong email format");
        }
        model.addAttribute("user", user);
        return "user-profile";
    }

    @RequestMapping(value = "/changeProfilePicture", method = RequestMethod.POST)
    public String changeProfilePicture(Principal principal, Model model,
                                       @RequestParam("profilePicture") MultipartFile profilePicture) {
        try {
            User user = userService.getByUsername(principal.getName());
            userService.addProfilePicture(user.getUsername(), profilePicture);
            model.addAttribute("success", "Picture changed successful");
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user-profile";
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
                                @RequestParam("password") String password) {
        User user = userService.getByUsername(principal.getName());
        postService.getPostsByUserId(user.getId());
        for (Post post : postService.getPostsByUserId(user.getId())) {
            postService.deletePost(post.getId());
        }
        if (password.equals(passwordEncoder.encode(user.getPassword()))) {
            userService.deleteUser(user.getId());

        } else {
            model.addAttribute("wrongEmailOrPassword", "Wrong email or password");
            return "deactivate-account";
        }
        return "index";
    }
}
