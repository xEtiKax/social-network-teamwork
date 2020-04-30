package com.example.demo.services.interfaces;

import com.example.demo.models.Comment;
import com.example.demo.models.DTO.CommentDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.LinkedHashSet;
import java.util.List;

public interface CommentService {
    Comment getById(long commentId);

    LinkedHashSet<Comment> getCommentsByPostId(long postId);

    Slice<Comment> getCommentsByPostIdWithPage(long postId, Pageable page);

    void createComment(Comment comments);

    List<Comment> getCommentsByUserId(long userId);

    void deleteComment(long id, String username);

    boolean checkIfCommentExist(long id);

    void updateComment(long id,CommentDTO commentDTO);
}
