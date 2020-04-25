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

    @GetMapping("post/{id}/postImage")
    public void renderPostImageFormDB(@PathVariable long id, HttpServletResponse response) throws IOException {
        Post post = postService.getPostById(id);
        renderImage(response, post.getPicture());
    }

    @GetMapping("user/{userId}/image")
    public String showUserUploadForm(@PathVariable long userId, Model model) {
        model.addAttribute("user", userService.getById(userId));
        return "user-profile";
    }

    @GetMapping("user/{id}/userImage")
    public void renderUserImageFormDB(@PathVariable long id, HttpServletResponse response) throws IOException {
        User user = userService.getById(id);
        renderImage(response, user.getPhoto().getData());
    }

    @GetMapping("public/user/{id}/userImage")
    public void renderPublicUserImageFormDB(@PathVariable long id, HttpServletResponse response) throws IOException {
        User user = userService.getById(id);
        renderImage(response, user.getPhoto().getData());
    }

    @GetMapping("user/{userId}/cover")
    public String showUserCoverUploadForm(@PathVariable long userId, Model model) {
        model.addAttribute("user", userService.getById(userId));
        return "user-profile";
    }

    @GetMapping("user/{id}/coverImage")
    public void renderUserCoverFormDB(@PathVariable long id, HttpServletResponse response) throws IOException {
        User user = userService.getById(id);
        renderImage(response, user.getCoverPhoto());
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

    private void renderImage(HttpServletResponse response, Byte[] photo) throws IOException {
        if (photo != null) {
            byte[] byteArray = new byte[photo.length];

            int i = 0;

            for (Byte wrappedByte : photo) {
                byteArray[i++] = wrappedByte;
            }
            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(byteArray);
            IOUtils.copy(is, response.getOutputStream());
        }
    }
}