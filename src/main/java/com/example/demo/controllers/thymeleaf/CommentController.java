package com.example.demo.controllers.thymeleaf;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.Comment;
import com.example.demo.models.DTO.CommentDTO;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.services.interfaces.CommentService;
import com.example.demo.services.interfaces.PostService;
import com.example.demo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/comment")
public class CommentController {

    PostService postService;
    UserService userService;
    CommentService commentService;

    @Autowired
    public CommentController(PostService postService, UserService userService, CommentService commentService) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @PostMapping("/add/{postId}")
    public String createComment(@PathVariable long postId, CommentDTO commentDTO, Principal principal) {
        createCommentPattern(postId, commentDTO, principal);
        return "redirect:/";
    }

    @PostMapping("/user/add/{postId}")
    public String createUserComment(@PathVariable long postId, CommentDTO commentDTO, Principal principal) {
        createCommentPattern(postId, commentDTO, principal);
        return "redirect:/user/showMyProfile";
    }

    @PostMapping("/update/{id}")
    public String updatePost(@PathVariable long id, @ModelAttribute CommentDTO commentDTO, Model model) {
        try {
            Comment comment = commentService.getById(id);
            commentService.updateComment(comment);
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        }
        return "redirect:/";
    }

    private void createCommentPattern(long postId, CommentDTO commentDTO, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        Post post = postService.getPostById(postId);
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setDescription(commentDTO.getDescription());
        commentService.createComment(comment);
    }
}
