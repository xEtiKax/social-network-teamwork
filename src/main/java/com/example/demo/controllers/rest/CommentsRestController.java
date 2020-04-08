package com.example.demo.controllers.rest;

import com.example.demo.models.Comments;
import com.example.demo.models.DTO.CommentsDTO;
import com.example.demo.models.DTO.UserDTO;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.services.CommentService;
import com.example.demo.services.PostService;
import com.example.demo.services.UserService;
import org.hibernate.validator.internal.constraintvalidators.bv.time.futureorpresent.FutureOrPresentValidatorForDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import javax.xml.stream.events.Comment;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentsRestController {

    private CommentService commentService;
    private PostService postService;
    private UserService userService;


    @Autowired
    public CommentsRestController(CommentService commentService, PostService postService,UserService userService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public Comments getCommentById(@PathVariable int id) {
        try {
            return commentService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/post/{postId}")
    public List<Comments> getCommentsByPostId(@PathVariable int postId) {
        try {
            return commentService.getCommentsByPostId(postId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @RequestMapping("/add")
    public Comments createComment(@RequestBody CommentsDTO commentsDto, @RequestHeader String requestUser) {
        Comments comment = new Comments();
        User user = userService.getByUsername(requestUser);
        Post post = postService.getPostById(commentsDto.getPostId());
        comment.setDescription(commentsDto.getDescription());
        comment.setPost(post);
        comment.setUser(user);
        commentService.createComment(comment);
        return comment;
    }

    @PutMapping("/update/{commentId}")
    public Comments updateComment(@RequestBody CommentsDTO commentDTO, @PathVariable int commentId, @RequestHeader String requestUser) {
        Comments comment = commentService.getById(commentId);
        try {
            comment.setDescription(commentDTO.getDescription());
            commentService.updateComment(comment);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return comment;
    }

    @DeleteMapping("/delete/{id}")
    public void deleteComment(@PathVariable int id) {
        Comments comment = commentService.getById(id);
        try {
            commentService.deleteComment(comment.getId());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
