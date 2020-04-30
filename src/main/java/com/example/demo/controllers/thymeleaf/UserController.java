package com.example.demo.controllers.thymeleaf;

import com.example.demo.exceptions.DuplicateEntityException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.exceptions.WrongEmailException;
import com.example.demo.exceptions.WrongPasswordException;
import com.example.demo.models.DTO.CommentDTO;
import com.example.demo.models.DTO.PostDTO;
import com.example.demo.models.Like;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.services.interfaces.ImageService;
import com.example.demo.services.interfaces.PostService;
import com.example.demo.services.interfaces.RequestService;
import com.example.demo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private RequestService requestService;
    private PostService postService;
    private PasswordEncoder passwordEncoder;
    private ImageService imageService;

    @Autowired
    public UserController(UserService userService, PostService postService,
                          PasswordEncoder passwordEncoder, RequestService requestService, ImageService imageService) {
        this.userService = userService;
        this.postService = postService;
        this.passwordEncoder = passwordEncoder;
        this.requestService = requestService;
        this.imageService = imageService;
    }

    @GetMapping("/showUserProfile/{userId}")
    public String showUserProfile(Model model, @PathVariable long userId, Principal principal) {
        User me = userService.getByUsername(principal.getName());
        User user = userService.getById(userId);

        boolean isSentRequest = requestService.checkIfRequestExist(me.getId(), userId);
        boolean isFriend = false;
        boolean isFriendAndIsSentRequest = true;
        boolean isVisiblePicture = true;
        int friendsCounter = user.getFriends().size();


        if (user.getPhoto() == null || !user.getFriends().contains(me) || !user.getPhoto().isPublic()) {
            isVisiblePicture = false;
        }
//        assert user.getPhoto() != null;
//        if (user.getPhoto().isPublic()) {
//            isVisiblePicture = true;
//        }

        if (!user.getFriends().contains(me) && !isSentRequest) {
            isFriendAndIsSentRequest = false;
        }
        if (user.getFriends().contains(me)) {
            isFriend = true;
        }
        List<Post> posts = checkLike(me, postService.getPostsByUserId(userId));

        model.addAttribute("isSentRequest", isSentRequest);
        model.addAttribute("userProfile", user);
        model.addAttribute("user", me);
        model.addAttribute("isFriend", isFriend);
        model.addAttribute("isFriendAndIsSentRequest", isFriendAndIsSentRequest);
        model.addAttribute("posts", posts);
        model.addAttribute("isInfo", false);
        model.addAttribute("friendsCounter", friendsCounter);
        model.addAttribute("isVisiblePicture", isVisiblePicture);
        model.addAttribute("newComment", new CommentDTO());
        return "user-profile";
    }

    @GetMapping("/showMyProfile")
    public String showMyProfile(Model model, Principal principal) {

        User user = userService.getByUsername(principal.getName());
        int friendsCounter = user.getFriends().size();

        List<Long> friendIds = getFriendIds(user);
        List<Post> posts = checkLike(user, postService.getMyFeed(friendIds));

        model.addAttribute("user", user);
        model.addAttribute("myPosts", posts);
        model.addAttribute("friendsCounter", friendsCounter);
        model.addAttribute("isInfo", false);
        model.addAttribute("newComment", new CommentDTO());
        return "my-profile-feed";
    }

    @GetMapping("/info/{userId}")
    public String viewUserInfo(@PathVariable long userId, Model model) {
        User user = userService.getById(userId);
        model.addAttribute("userProfile", user);
        model.addAttribute("isInfo", true);
        return "user-profile";
    }

    @GetMapping("/my/info/{userId}")
    public String viewMyInfo(@PathVariable long userId, Model model) {
        User user = userService.getById(userId);
        model.addAttribute("user", user);
        model.addAttribute("isInfo", true);
        return "my-profile-feed";
    }

    @GetMapping("/showMyFriends/{userId}")
    public String showFriends(@PathVariable long userId, Model model) {
        User user = userService.getById(userId);
        List<User> friends = userService.getUserFriends(user.getId());
        model.addAttribute("friends", friends);
        return "friends";
    }

    @PostMapping("/searchUser")
    public String searchUser(@RequestParam(value = "username") String username, Model model, Principal principal) {
        List<User> result = userService.getByNameLikeThis(username);
        User user = userService.getByUsername(principal.getName());
        model.addAttribute("result", result);
        model.addAttribute("user", user);
        return "search-result";
    }

    @GetMapping("/showAllUsers")
    public String showUsers(Model model, Principal principal) {
        model.addAttribute("users", userService.getAll());
        User user = userService.getByUsername(principal.getName());
        model.addAttribute("user", user);
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

    @PostMapping("/changePassword")
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

    @GetMapping("/edit")
    public String editUserDetails(Model model,
                                  Principal principal) {
        User user = userService.getByUsername(principal.getName());
        model.addAttribute("user", user);
        return "user-settings";
    }


    @PostMapping("/updateProfile")
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

    @GetMapping("/deleteProfile")
    public String deleteUserProfile(Model model,
                                    Principal principal) {
        User user = userService.getByUsername(principal.getName());
        model.addAttribute("user", user);
        return "deactivate-account";
    }

    @PostMapping("/deleteProfile")
    public String deleteProfile(Principal principal,
                                Model model,
                                @RequestParam("password") String password,
                                HttpServletRequest request) {
        User user = userService.getByUsername(principal.getName());

        for (Post post : postService.getPostsByUserId(user.getId())) {
            postService.deletePost(post.getId(), user, request.isUserInRole("ROLE_ADMIN"));
        }
        boolean passwordMatch = passwordEncoder.matches(password, user.getPassword());
        if (passwordMatch) {
            userService.deleteUser(user.getId());
        } else {
            model.addAttribute("wrongPassword", "Wrong password");
            return "deactivate-account";
        }
        return "index";
    }

    @GetMapping("/post/new")
    public String showUserPosts(Model model, Principal principal) {
        try {
            User user = userService.getByUsername(principal.getName());
            model.addAttribute("user", user);
            model.addAttribute("friendsCounter", user.getFriends().size());
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        }
        model.addAttribute("post", new PostDTO());

        return "my-profile-feed";
    }

    public List<Long> getFriendIds(User user) {
        List<Long> friendIds = new ArrayList<>();
        friendIds.add(user.getId());
        for (User u : user.getFriends()) {
            friendIds.add(u.getId());
        }
        return friendIds;
    }

    public List<Post> checkLike(User user, List<Post> posts) {
        for (Post post : posts) {
            for (Like like : post.getLikes()) {
                if (like.getUser().getId() == user.getId()) {
                    post.setLiked(true);
                }
            }
        }
        return posts;
    }


    @GetMapping("/photoPrivacy")
    public String photoPrivacy(Principal principal) {
        User user = userService.getByUsername(principal.getName());
        if (user.getPhoto().isPublic()) {
            imageService.setPrivacy(user.getPhoto(), false);
        } else {
            imageService.setPrivacy(user.getPhoto(), true);
        }
        return "redirect:/user/privacy";
    }
}
