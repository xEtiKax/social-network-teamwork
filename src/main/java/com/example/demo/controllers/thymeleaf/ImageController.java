package com.example.demo.controllers.thymeleaf;

import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.services.interfaces.ImageService;
import com.example.demo.services.interfaces.PostService;
import com.example.demo.services.interfaces.UserService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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

    @GetMapping("post/{id}/image")
    public String showPostUploadForm(@PathVariable long id, Model model) {
        model.addAttribute("post", postService.getPostById(id));
        return "uploadPostPhoto";
    }

    @GetMapping("post/{id}/postImage")
    public void renderPostImageFormDB(@PathVariable long id, HttpServletResponse response) throws IOException {
        Post post = postService.getPostById(id);
        renderImage(response, post.getPicture());
    }

    @PostMapping("post/{id}/image")
    public String handlePostImagePost(@PathVariable long id, @RequestParam("imageFile") MultipartFile file) {
        imageService.savePostPhoto(id, file);
        return "redirect:/posts";
    }

    @PostMapping("user/{userId}/image")
    public String handleUserImagePost(@PathVariable long userId, @RequestParam("imageFile") MultipartFile file) {
        imageService.saveUserPhoto(userId, file);
        return "redirect:/user";
    }

    @GetMapping("user/{userId}/image")
    public String showUserUploadForm(@PathVariable long userId, Model model) {
        model.addAttribute("user", userService.getById(userId));
        return "user-profile";
    }

    @GetMapping("user/{id}/userImage")
    public void renderUserImageFormDB(@PathVariable long id, HttpServletResponse response) throws IOException {
        User user = userService.getById(id);
        renderImage(response, user.getPhoto());
    }

    @PostMapping("user/{userId}/cover")
    public String handleUserImageCover(@PathVariable long userId, @RequestParam("imageFile") MultipartFile file) {
        imageService.saveUserCover(userId, file);
        return "redirect:/user";
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