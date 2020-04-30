package com.example.demo.controllers.rest;

import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.models.Comment;
import com.example.demo.models.DTO.CommentDTO;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.services.interfaces.CommentService;
import com.example.demo.services.interfaces.PostService;
import com.example.demo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashSet;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    private CommentService commentService;
    private PostService postService;
    private UserService userService;


    @Autowired
    public CommentRestController(CommentService commentService, PostService postService, UserService userService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable long id) {
        try {
            return commentService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/byUser/{userId}")
    public List<Comment> getCommentsByUserId(@PathVariable long userId) {
        try {
            return commentService.getCommentsByUserId(userId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This user don't have comments");
        }
    }

    @GetMapping("/post/{postId}")
    public LinkedHashSet<Comment> getCommentsByPostId(@PathVariable long postId) {
        try {
            return commentService.getCommentsByPostId(postId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/add")
    public Comment createComment(@RequestBody CommentDTO commentDto, @RequestHeader String requestUser) {
        Comment comment = new Comment();
        User user = userService.getByUsername(requestUser);
        Post post = postService.getPostById(commentDto.getPostId());
        comment.setDescription(commentDto.getDescription());
        comment.setPost(post);
        comment.setUser(user);
        commentService.createComment(comment);
        return comment;
    }

    @PutMapping("/update/{commentId}")
    public Comment updateComment(@RequestBody CommentDTO commentDTO, @PathVariable long commentId) {
        Comment comment = commentService.getById(commentId);
        try {
            comment.setDescription(commentDTO.getDescription());
            commentService.updateComment(commentId,commentDTO);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return comment;
    }

    @DeleteMapping("/delete/{commentId}")
    public void deleteComment(@PathVariable long commentId, @RequestHeader String user, HttpServletRequest request) {
        User requestUser = userService.getByUsername(user);
        Comment comment = commentService.getById(commentId);
        try {
            commentService.deleteComment(comment.getId(), requestUser.getUsername(),request.isUserInRole("ROLE_ADMIN"));
        } catch (AuthorizationException a) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, a.getMessage());
        }
    }
}
