package com.example.demo.repositories;

import com.example.demo.models.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {

    @Query(value = "SELECT * FROM social_network.posts WHERE id = ?1 AND enabled != 0", nativeQuery = true)
    Post getById(long id);

    @Query(value = "select * from social_network.posts where posts.created_by = ?1 AND posts.enabled != 0 ORDER BY created_at DESC ", nativeQuery = true)
    List<Post> getPostsByUserId(long userId);

    @Query(value = "SELECT *\n" +
            "FROM social_network.posts p\n" +
            "         LEFT JOIN social_network.comments c ON p.id = c.post_id\n" +
            "         LEFT JOIN social_network.likes l ON p.id = l.post_id\n" +
            "WHERE p.is_public != 0 AND p.enabled != 0\n" +
            "GROUP BY p.id, p.created_at\n" +
            "ORDER BY COUNT(DISTINCT l.like_id) DESC, COUNT(DISTINCT c.id) DESC, p.created_at DESC;", nativeQuery = true)
    List<Post> getAllPublicPosts();


    @Query(value = "SELECT * FROM social_network.posts p\n" +
            "LEFT JOIN social_network.comments c ON p.id = c.post_id\n" +
            "LEFT JOIN social_network.likes l ON p.id = l.post_id\n" +
            "WHERE p.enabled != 0 AND created_by IN (:friendIds)\n" +
            "GROUP BY p.id, p.created_at\n" +
            "ORDER BY COUNT(DISTINCT l.like_id) DESC, COUNT(DISTINCT c.id) DESC, p.created_at DESC;", nativeQuery = true)
    List<Post> getMyFeed(@Param("friendIds") List<Long> friendIds);

}
