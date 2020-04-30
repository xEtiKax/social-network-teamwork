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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

    @GetMapping("/edit/{commentId}")
    public String showEditCommentForm(@PathVariable int commentId, Model model, Principal principal) {
        try {
            User user = userService.getByUsername(principal.getName());
            model.addAttribute("user", user);
            model.addAttribute("friendsCounter", user.getFriends().size());
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        }
        Comment comment = commentService.getById(commentId);
        Post post = postService.getPostById(comment.getPost().getId());
        model.addAttribute("comment", comment);
        model.addAttribute("post", post);
        return "index";
    }

    @PostMapping("/edit/{id}")
    public String updateComment(@PathVariable long id, @ModelAttribute CommentDTO commentDTO, Model model) {
        try {
            commentService.updateComment(id, commentDTO);
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        }
        return "redirect:/";
    }

    @PostMapping("/delete/{commentId}")
    public String deleteComment(@PathVariable long commentId, Model model, Principal principal) {
        try {
            commentService.deleteComment(commentId, principal.getName());
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        } catch (AuthorizationException auth) {
            model.addAttribute("auth", auth);
        }
        return "redirect:/";
    }

    @PostMapping("/user/add/{postId}")
    public String createUserComment(@PathVariable long postId, CommentDTO commentDTO, Principal principal) {
        createCommentPattern(postId, commentDTO, principal);
        return "redirect:/user/showMyProfile";
    }

    @GetMapping("/edit/myprofile/{commentId}")
    public String showEditCommentFormMyProfile(@PathVariable long commentId, Model model, Principal principal) {
        try {
            User user = userService.getByUsername(principal.getName());
            model.addAttribute("user", user);
            model.addAttribute("friendsCounter", user.getFriends().size());
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        }
        model.addAttribute("comment", commentService.getById(commentId));
        model.addAttribute("post", new PostDTO());
        return "my-profile-feed";
    }

    @PostMapping("/edit/myprofile/{id}")
    public String updateCommentMyProfile(@PathVariable long id, @ModelAttribute CommentDTO commentDTO, Model model) {
        try {
            commentService.updateComment(id, commentDTO);
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        }
        return "redirect:/user/showMyProfile";
    }

    @PostMapping("/delete/myprofile/{commentId}")
    public String deleteCommentMyProfile(@PathVariable long commentId, Model model, Principal principal) {
        try {
            commentService.deleteComment(commentId, principal.getName());
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        } catch (AuthorizationException auth) {
            model.addAttribute("auth", auth);
        }
        return "redirect:/user/showMyProfile";
    }

    @PostMapping("/profile/add/{postId}")
    public String createUserProfileComment(@PathVariable long postId, CommentDTO commentDTO, Principal principal) {
        createCommentPattern(postId, commentDTO, principal);
        long userId = postService.getPostById(postId).getCreatedBy().getId();
        return "redirect:/user/showUserProfile/" + userId;
    }

    @GetMapping("/edit/profile/{userId}/{commentId}")
    public String showEditCommentForm(@PathVariable long userId, @PathVariable long commentId, Model model, Principal principal) {
        model.addAttribute("me", userService.getByUsername(principal.getName()));
        model.addAttribute("user", userService.getById(userId));
        model.addAttribute("comment", commentService.getById(commentId));
        return "user-profile";
    }

    @PostMapping("/edit/profile/{id}")
    public String updateCommentProfile(@PathVariable long id, @ModelAttribute CommentDTO commentDTO, Model model) {
        try {
            commentService.updateComment(id, commentDTO);
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        }
        return "redirect:/user/showUserProfile/" + commentService.getById(id).getPost().getCreatedBy().getId();
    }

    @PostMapping("/delete/profile/{commentId}")
    public String deleteCommentProfile(@PathVariable long commentId, Model model, Principal principal) {
        long userId = commentService.getById(commentId).getPost().getCreatedBy().getId();
        try {
            commentService.deleteComment(commentId, principal.getName());
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e);
            return "error";
        } catch (AuthorizationException auth) {
            model.addAttribute("auth", auth);
        }
        return "redirect:/user/showUserProfile/" + userId;
    }

    @GetMapping("/list/{postId}")
    public String showPostComments(@PathVariable long postId, Model model, @RequestParam int si, @RequestParam int ps ) {
        Post post = postService.getPostById(postId);
        Slice<Comment> comments = commentService.getCommentsByPostIdWithPage(postId, PageRequest.of(si, ps, Sort.by("dateTime").ascending()));
        LinkedHashSet<Comment> linkedComments = new LinkedHashSet(comments.getContent());
        post.setComments(linkedComments);
        model.addAttribute("post", post);
        model.addAttribute("newComment", new CommentDTO());
        return "comments :: commentsList";
    }

    private void createCommentPattern(long postId, CommentDTO commentDTO, Principal principal) {
        Post post = postService.getPostById(postId);
        User user = userService.getByUsername(principal.getName());
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setDescription(commentDTO.getDescription());
        commentService.createComment(comment);
    }
}
