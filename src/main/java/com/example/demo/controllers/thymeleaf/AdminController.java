package com.example.demo.controllers.thymeleaf;

import com.example.demo.exceptions.DuplicateEntityException;
import com.example.demo.exceptions.WrongEmailException;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.services.interfaces.PostService;
import com.example.demo.services.interfaces.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserService userService;
    private PostService postService;

    public AdminController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/edit/{userId}")
    public String editUserDetails(@PathVariable long userId, Model model) {
        User user = userService.getById(userId);
        model.addAttribute("user", user);
        return "user-settings-admin";
    }

    @PostMapping("/deleteProfile/{userId}")
    public String deleteProfile(@PathVariable long userId, HttpServletRequest request, Model model) {
        User user = userService.getById(userId);
        if (request.isUserInRole("ROLE_ADMIN")) {
            for (Post post : postService.getPostsByUserId(user.getId())) {
                postService.deletePost(post.getId(), user, request.isUserInRole("ROLE_ADMIN"));
            }
            userService.deleteUser(user.getId());
        } else {
            model.addAttribute("error", "You have not permissions");
        }
        return "redirect:/user/showAllUsers";
    }

    @PostMapping("/updateProfile/{userId}")
    public String updateUserProfile(@PathVariable long userId,
                                    @RequestParam("firstName") String firstName,
                                    @RequestParam("lastName") String lastName,
                                    @RequestParam("email") String email,
                                    @RequestParam("age") int age,
                                    @RequestParam("jobTitle") String jobTitle,
                                    HttpServletRequest request,
                                    Model model) {
        User user = userService.getById(userId);
        if (request.isUserInRole("ROLE_ADMIN")) {
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
        model.addAttribute("no permissions", "You are not Admin");
        return "redirect:/user/showAllUsers";
    }
}
