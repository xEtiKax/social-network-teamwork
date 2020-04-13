package com.example.demo.services;

import com.example.demo.models.Comment;

import java.util.List;

public interface CommentService {
    Comment getById(long commentId);

    List<Comment> getCommentsByPostId(long postId);

    Comment createComment(Comment comments);

    List<Comment> getCommentsByUserId(long userId);

    Comment updateComment(Comment comments);

    void deleteComment(long id, String username);


}
