package com.example.demo.services;

import com.example.demo.models.Comments;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentService {
    Comments getById(int commentId);

    List<Comments>getCommentsByPostId(int postId);

    Comments createComment(Comments comments);

    Comments updateComment(Comments comments);

    void deleteComment(int id);


}
