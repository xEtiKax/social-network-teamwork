package com.example.demo.controllers.thymeleaf;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.services.interfaces.ImageService;
import com.example.demo.services.interfaces.PostService;
import com.example.demo.services.interfaces.UserService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;

@Controller
public class ImageController {
    ImageService imageService;
    PostService postService;
    UserService userService;

    @Autowired
    public ImageController(ImageService imageService, PostService postService, UserService userService) {
        this.imageService = imageService;
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("user/{id}/userImage")
    public void renderUserImageFormDB(@PathVariable long id, HttpServletResponse response) throws IOException {
        User user = userService.getById(id);
        imageService.renderImage(response, user.getPhoto().getData());
    }

    @GetMapping("public/user/{id}/userImage")
    public void renderPublicUserImageFormDB(@PathVariable long id, HttpServletResponse response) throws IOException {
        User user = userService.getById(id);
        imageService.renderImage(response, user.getPhoto().getData());
    }

    @GetMapping("user/{id}/coverImage")
    public void renderUserCoverFormDB(@PathVariable long id, HttpServletResponse response) throws IOException {
        User user = userService.getById(id);
        imageService.renderImage(response, user.getCoverPhoto());
    }

    @PostMapping("/user/changeProfilePicture")
    public String changeProfilePicture(Principal principal, Model model,
                                       @RequestParam("profilePicture") MultipartFile profilePicture) {
        try {
            User user = userService.getByUsername(principal.getName());
            userService.addProfilePicture(user.getUsername(), profilePicture);
            model.addAttribute("success", "Picture changed successful");
        } catch (EntityNotFoundException | IOException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/user/showMyProfile";
    }

    @PostMapping("/user/changeCoverPhoto")
    public String changeCoverPhoto(Principal principal, Model model,
                                   @RequestParam("coverPhoto") MultipartFile coverPhoto) {
        try {
            User user = userService.getByUsername(principal.getName());
            userService.addCoverPhoto(user.getUsername(), coverPhoto);
            model.addAttribute("success", "Picture changed successful");
        } catch (EntityNotFoundException | IOException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/user/showMyProfile";
    }
}


