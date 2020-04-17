package com.example.demo.repositories;

import com.example.demo.models.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;

public interface LikeRepository extends JpaRepository<Like, Serializable> {

//    @Query("select l from Like l where l.user.id = ?1 and l.post.id = ?1")
//    Like getLikeByUserIdAndPostId(long userId, long postId);

    Like getLikeByUser_IdAndPost_Id(long userId, long postId);

    @Query(value = "select count(*) from social_network.likes where post_id = ?1", nativeQuery = true)
    int getPostLikes(long postId);

    @Query(value = "SELECT * FROM social_network.likes WHERE user_id = ?1 AND post_id = ?2", nativeQuery = true)
    boolean existsByUniquePair(long userId, long postId);

}
