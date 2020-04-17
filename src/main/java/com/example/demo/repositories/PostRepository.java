package com.example.demo.repositories;

import com.example.demo.models.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.List;

public interface PostRepository extends CrudRepository<Post, Serializable> {

    @Query(value = "SELECT * FROM social_network.posts WHERE id = ?1 AND enabled != 0", nativeQuery = true)
    Post getById(long id);

    @Query(value = "select * from social_network.posts join social_network.users u on posts.created_by = u.id where u.id = ?1 AND posts.enabled != 0 ORDER BY created_at DESC ", nativeQuery = true)
    List<Post> getPostsByUserId(long userId);

    @Query(value = "SELECT * FROM social_network.posts WHERE is_public != 0 AND enabled != 0 ORDER BY created_at DESC ", nativeQuery = true)
    List<Post> getAllPublicPosts();
}
