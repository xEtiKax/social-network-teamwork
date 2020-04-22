package com.example.demo.controllers.thymeleaf;

import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.Comment;
import com.example.demo.models.DTO.CommentDTO;
import com.example.demo.models.DTO.PostDTO;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.services.interfaces.CommentService;
import com.example.demo.services.interfaces.PostService;
import com.example.demo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
//    @GetMapping("/new")
//    public String showNewCommentForm(Model model) {
//        model.addAttribute("comment", new CommentDTO());
//        return "index";
//    }

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

    @PostMapping("/profile/add/{postId}/{userId}")
    public String createUserProfileComment(@PathVariable long postId, @PathVariable long userId,
                                           CommentDTO commentDTO, Principal principal) {
        createCommentPattern(postId, commentDTO, principal);
        return "redirect:/user/showUserProfile/" + userId;
    }

    @PostMapping("/update/{id}")
    public String updateComment(@PathVariable long id, @ModelAttribute CommentDTO commentDTO, Model model) {
        try {
            commentService.updateComment(id,commentDTO);
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/delete/{commentId}", method = RequestMethod.GET)
    public String deleteComment(@PathVariable long commentId, Model model,
                                    Principal principal) {
        User user = userService.getByUsername(principal.getName());
        try {
            commentService.deleteComment(commentId, user.getUsername());
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        }catch (AuthorizationException auth) {
            model.addAttribute("auth",auth);
        }
        return "redirect:/";
    }

    @GetMapping("/edit/profile/{commentId}")
    public String showEditCommentForm(@PathVariable long commentId, Model model) {
        model.addAttribute("comment", commentService.getById(commentId));
        return "index";
    }

    @RequestMapping(value = "/delete/profile/{commentId}", method = RequestMethod.GET)
    public String deleteCommentProfile(@PathVariable long commentId, Model model,
                                Principal principal) {
        User user = userService.getByUsername(principal.getName());
        try {
            commentService.deleteComment(commentId, user.getUsername());
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        }catch (AuthorizationException auth) {
            model.addAttribute("auth",auth);
        }
        return "redirect:/showUserProfile/" + commentService.getById(commentId).getPost().getCreatedBy().getId();
    }

    @GetMapping("/edit/{commentId}")
    public String showEditCommentFormProfile(@PathVariable long commentId, Model model) {
        model.addAttribute("comment", commentService.getById(commentId));
        return "redirect:/showUserProfile/" + commentService.getById(commentId).getPost().getCreatedBy().getId();
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
