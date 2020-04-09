package com.example.demo.services;

import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.Comments;
import com.example.demo.models.User;
import com.example.demo.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private UserService userService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    @Override
    public Comments getById(int commentId) {
        return commentRepository.getByCommentId(commentId);
    }

    @Override
    public List<Comments> getCommentsByPostId(int postId) {
        return commentRepository.getCommentsByPostId(postId);
    }

    @Override
    public Comments createComment(Comments comments) {
        commentRepository.save(comments);
        return comments;
    }

    @Override
    public List<Comments> getCommentsByUserId(int userId) {
        return commentRepository.getCommentsByUserId(userId);
    }

    @Override
    public Comments updateComment(Comments comments) {
        commentRepository.save(comments);
        return comments;
    }

    @Override
    public void deleteComment(int id, String username) {
        User user = userService.getByUsername(username);
        List<Comments> userComments = getCommentsByUserId(user.getId());
        if (userComments.contains(commentRepository.getByCommentId(id))) {
            commentRepository.deleteById(id);
        } else {
            throw new AuthorizationException("You have not permissions to delete this comment");
        }
    }
}
