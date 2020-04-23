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
    @GetMapping("/edit/{commentId}")
    public String showUpdateCommentForm(@PathVariable int commentId, Model model) {
        Comment comment = commentService.getById(commentId);
        model.addAttribute("comment",comment);
        return "index";
    }

    @GetMapping("/edit/profile/{commentId}")
    public String showEditCommentForm(@PathVariable long commentId, Model model) {
        model.addAttribute("comment", commentService.getById(commentId));
        return "index";
    }

    @GetMapping("/edit/myprofile/{commentId}")
    public String showEditCommentFormMyProfile(@PathVariable long commentId, Model model) {
        model.addAttribute("comment", commentService.getById(commentId));
        return "index";
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

    @PostMapping("/profile/add/{postId}")
    public String createUserProfileComment(@PathVariable long postId,
                                           CommentDTO commentDTO, Principal principal) {
        createCommentPattern(postId, commentDTO, principal);
        long userId = postService.getPostById(postId).getCreatedBy().getId();
        return "redirect:/user/showUserProfile/" + userId;
    }

    @PostMapping("/edit/{id}")
    public String updateComment(@PathVariable long id, @ModelAttribute CommentDTO commentDTO, Model model) {
        try {
            commentService.updateComment(id,commentDTO);
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        }
        return "redirect:/";
    }

    @PostMapping("/edit/profile/{id}")
    public String updateCommentProfile(@PathVariable long id, @ModelAttribute CommentDTO commentDTO, Model model) {
        try {
            commentService.updateComment(id,commentDTO);
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        }
        return "redirect:/user/showUserProfile/" + commentService.getById(id).getPost().getCreatedBy().getId();
    }

    @PostMapping("/edit/myprofile/{id}")
    public String updateCommentMyProfile(@PathVariable long id, @ModelAttribute CommentDTO commentDTO, Model model) {
        try {
            commentService.updateComment(id,commentDTO);
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        }
        return "redirect:/user/showUserProfile/" + commentService.getById(id).getPost().getCreatedBy().getId();
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



    @RequestMapping(value = "/delete/profile/{commentId}", method = RequestMethod.GET)
    public String deleteCommentProfile(@PathVariable long commentId, Model model,
                                Principal principal) {
        User user = userService.getByUsername(principal.getName());
        long userId = commentService.getById(commentId).getPost().getCreatedBy().getId();
        try {
            commentService.deleteComment(commentId, user.getUsername());
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        }catch (AuthorizationException auth) {
            model.addAttribute("auth",auth);
        }
        return "redirect:/user/showUserProfile/" + userId;
    }

    @RequestMapping(value = "/delete/myprofile/{commentId}", method = RequestMethod.GET)
    public String deleteCommentMyProfile(@PathVariable long commentId, Model model,
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
        return "redirect:/user/showMyProfile";
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
