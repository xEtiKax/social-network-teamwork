package com.example.demo.services.interfaces;

import com.example.demo.models.Comment;
import com.example.demo.models.DTO.CommentDTO;

import java.util.List;

public interface CommentService {
    Comment getById(long commentId);

    List<Comment> getCommentsByPostId(long postId);

    void createComment(Comment comments);

    List<Comment> getCommentsByUserId(long userId);

    void deleteComment(long id, String username);

    boolean checkIfCommentExist(long id);

    void updateComment(long id,CommentDTO commentDTO);
}
