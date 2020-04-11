package com.example.demo.repositories;

import com.example.demo.models.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.io.Serializable;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Serializable> {

    @Query("SELECT c FROM Comment c where c.post.id = ?1")
    List<Comment> getCommentsByPostId(int postId);

    @Query("SELECT c FROM Comment c WHERE c.id =?1")
    Comment getByCommentId(int id);

    @Query("SELECT c FROM Comment c where c.user.id = ?1")
    List<Comment> getCommentsByUserId(int userId);
}
