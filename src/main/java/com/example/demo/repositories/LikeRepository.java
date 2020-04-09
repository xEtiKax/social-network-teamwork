package com.example.demo.repositories;

import com.example.demo.models.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;

public interface LikeRepository extends JpaRepository<Like, Serializable> {

    @Query("select l from Like l where l.user.id = ?1 and l.post.id = ?1")
    Like getLikeByUserIdAndPostId(int userId, int postId);

    @Query(value = "select count(*) from social_network.likes where post_id = ?1", nativeQuery = true)
    int getPostLikes(int postId);

}
