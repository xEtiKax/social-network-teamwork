//package com.example.demo.controllers.thymeleaf;
//
//import com.example.demo.models.User;
//import com.example.demo.services.ImageService;
//import com.example.demo.services.UserService;
//import org.apache.tomcat.util.http.fileupload.IOUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//@Controller
//public class ImageController {
//    ImageService imageService;
//    PostService postService;
//    UserService userService;
//
//    @Autowired
//    public ImageController(ImageService imageService, PostService postService, UserService userService) {
//        this.imageService = imageService;
//        this.postService = postService;
//        this.userService = userService;
//    }
//
//    @GetMapping("post/{id}/image")
//    public String showPostUploadForm(@PathVariable int id, Model model) {
//        model.addAttribute("post", postService.getById(id));
//        return "uploadPostPhoto";
//    }
//
//    @GetMapping("post/{id}/postImage")
//    public void renderPostImageFormDB(@PathVariable int id, HttpServletResponse response) throws IOException {
//        Post post = postService.getById(id);
//        renderImage(response, post.getPhoto());
//    }
//
//    @PostMapping("post/{id}/image")
//    public String handlePostImagePost(@PathVariable int id, @RequestParam("imageFile") MultipartFile file) {
//        imageService.savePostPhoto(id, file);
//        return "redirect:/posts";
//    }
//
//    @PostMapping("user/{userId}/image")
//    public String handleUserImagePost(@PathVariable int userId, @RequestParam("imageFile") MultipartFile file) {
//        imageService.saveUserPhoto(userId, file);
//        return "redirect:/user";
//    }
//
//    @GetMapping("user/{userId}/image")
//    public String showUserUploadForm(@PathVariable int userId, Model model) {
//        model.addAttribute("user", userService.getById(userId));
//        return "uploadUserPhoto";
//    }
//
//    @GetMapping("user/{id}/userImage")
//    public void renderUserImageFormDB(@PathVariable int id, HttpServletResponse response) throws IOException {
//        User user = userService.getById(id);
//        renderImage(response, user.getPhoto());
//    }
//
//    private void renderImage(HttpServletResponse response, Byte[] photo) throws IOException {
//        if (photo != null) {
//            byte[] byteArray = new byte[photo.length];
//
//            int i = 0;
//
//            for (Byte wrappedByte : photo) {
//                byteArray[i++] = wrappedByte;
//            }
//            response.setContentType("image/jpeg");
//            InputStream is = new ByteArrayInputStream(byteArray);
//            IOUtils.copy(is, response.getOutputStream());
//        }
//    }
//}