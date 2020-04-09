package com.example.demo.repositories;

import com.example.demo.models.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.List;

public interface PostRepository extends CrudRepository<Post, Serializable> {

    @Query(value = "SELECT * FROM social_network.posts WHERE id = ?1 AND enabled != 0", nativeQuery = true)
    Post getPostById(int id);

    @Query(value = "select * from posts join users u on posts.created_by = u.id where u.id = ?1", nativeQuery = true)
    List<Post> getPostsByUserId(int userId);
}
