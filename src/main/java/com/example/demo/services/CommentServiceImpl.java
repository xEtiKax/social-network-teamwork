package com.example.demo.services;

import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.Comment;
import com.example.demo.models.DTO.CommentDTO;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.interfaces.CommentService;
import com.example.demo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    public static final String COMMENT_DOES_NOT_EXISTS = "Comment does not exists.";
    private CommentRepository commentRepository;
    private UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Comment getById(long commentId) {
        throwIfCommentDoesNotExists(commentId);
        return commentRepository.getByCommentId(commentId);
    }

    @Override
    public LinkedHashSet<Comment> getCommentsByPostId(long postId) {
        return commentRepository.getCommentsByPostId(postId);
    }

    @Override
    public Slice<Comment> getCommentsByPostIdWithPage(long postId, Pageable page) {
        return commentRepository.findAllByPostId(postId, page);
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
    public void deleteComment(long commentId, String username, boolean isAdmin) {
        throwIfCommentDoesNotExists(commentId);
        User user = userRepository.getUserByUsername(username);
        Comment comment = commentRepository.getByCommentId(commentId);
        canUserDeleteUpdateComment(comment, user, isAdmin);
        Post post = comment.getPost();

        user.removeComment(comment);
        post.removeComment(comment);
        commentRepository.deleteById(commentId);
    }

    private void throwIfCommentDoesNotExists(long id) {
        if (!checkIfCommentExist(id)) {
            throw new EntityNotFoundException(
                    String.format(COMMENT_DOES_NOT_EXISTS, id));
        }
    }

    private void canUserDeleteUpdateComment(Comment comment, User user, boolean isAdmin) {
        if (!user.getUsername().equals(comment.getUser().getUsername()) && !isAdmin) {
            throw new AuthorizationException();
        }
    }

    @Override
    public boolean checkIfCommentExist(long id) {
        return commentRepository.existsById(id);
    }
}
