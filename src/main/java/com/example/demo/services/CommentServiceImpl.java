package com.example.demo.services;

import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.Comment;
import com.example.demo.models.DTO.CommentDTO;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.services.interfaces.CommentService;
import com.example.demo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    public static final String COMMENT_DOES_NOT_EXISTS = "Comment does not exists.";
    private CommentRepository commentRepository;
    private UserService userService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    @Override
    public Comment getById(long commentId) {
        return commentRepository.getByCommentId(commentId);
    }

    @Override
    public List<Comment> getCommentsByPostId(long postId) {
        return commentRepository.getCommentsByPostId(postId);
    }

    @Override
    public void createComment(Comment comments) {
        commentRepository.save(comments);
    }

    @Override
    public List<Comment> getCommentsByUserId(long userId) {
        return commentRepository.getCommentsByUserId(userId);
    }


    @Override
    public void updateComment(long id, CommentDTO commentDTO) {
        throwIfCommentDoesNotExists(id);
        Comment commentToUpdate = getById(id);
        commentToUpdate.setDescription(commentDTO.getDescription());
        commentRepository.save(commentToUpdate);
    }

    @Override
    public void deleteComment(long commentId, String username) {
        User user = userService.getByUsername(username);
        Comment comment = commentRepository.getByCommentId(commentId);
        Post post = comment.getPost();
        if (user.getUsername().equals(comment.getUser().getUsername())) {
           user.removeComment(comment);
           post.removeComment(comment);
           commentRepository.deleteById(commentId);
        } else {
            throw new AuthorizationException("You have not permissions to delete this comment");
        }
    }

    private void throwIfCommentDoesNotExists(long id) {
        if (!checkIfCommentExist(id)) {
            throw new EntityNotFoundException(
                    String.format(COMMENT_DOES_NOT_EXISTS, id));
        }
    }

    @Override
    public boolean checkIfCommentExist(long id) {
        return commentRepository.existsById(id);
    }
}
