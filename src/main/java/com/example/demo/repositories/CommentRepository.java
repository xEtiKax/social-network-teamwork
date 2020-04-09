package com.example.demo.repositories;

import com.example.demo.models.Comments;

import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


import java.io.Serializable;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comments, Serializable> {

    @Query("SELECT c FROM Comments c where c.post.id = ?1")
    List<Comments> getCommentsByPostId(int postId);

    @Query("SELECT c FROM Comments c WHERE c.id =?1")
    Comments getByCommentId(int id);

    @Query("SELECT c FROM Comments c where c.user.id = ?1")
    List<Comments> getCommentsByUserId(int userId);
}
