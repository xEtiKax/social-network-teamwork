package com.example.demo.services;

import com.example.demo.models.Comment;

import java.util.List;

public interface CommentService {
    Comment getById(int commentId);

    List<Comment> getCommentsByPostId(int postId);

    Comment createComment(Comment comments);

    List<Comment> getCommentsByUserId(int userId);

    Comment updateComment(Comment comments);

    void deleteComment(int id, String username);


}
