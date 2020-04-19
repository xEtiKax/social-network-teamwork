package com.example.demo.services.interfaces;

import com.example.demo.models.Comment;

import java.util.List;

public interface CommentService {
    Comment getById(long commentId);

    List<Comment> getCommentsByPostId(long postId);

    void createComment(Comment comments);

    List<Comment> getCommentsByUserId(long userId);

    void updateComment(Comment comments);

    void deleteComment(long id, String username);


}
