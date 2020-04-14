package com.example.demo.controllers.rest;

import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.models.Comment;
import com.example.demo.models.DTO.CommentsDTO;
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
    public List<Comment> getCommentsByPostId(@PathVariable long postId) {
        try {
            return commentService.getCommentsByPostId(postId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/add")
    public Comment createComment(@RequestBody CommentsDTO commentsDto, @RequestHeader String requestUser) {
        Comment comment = new Comment();
        User user = userService.getByUsername(requestUser);
        Post post = postService.getPostById(commentsDto.getPostId());
        comment.setDescription(commentsDto.getDescription());
        comment.setPost(post);
        comment.setUser(user);
        commentService.createComment(comment);
        return comment;
    }

    @PutMapping("/update/{commentId}")
    public Comment updateComment(@RequestBody CommentsDTO commentDTO, @PathVariable long commentId, @RequestHeader String requestUser) {
        Comment comment = commentService.getById(commentId);
        try {
            comment.setDescription(commentDTO.getDescription());
            commentService.updateComment(comment);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return comment;
    }

    @DeleteMapping("/delete/{commentId}")
    public void deleteComment(@PathVariable long commentId, @RequestHeader String user) {
        User requestUser = userService.getByUsername(user);
        Comment comment = commentService.getById(commentId);
        try {
            commentService.deleteComment(comment.getId(), requestUser.getUsername());
        } catch (AuthorizationException a) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, a.getMessage());
        }
    }
}
