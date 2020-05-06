package com.example.demo.repositories;

import com.example.demo.models.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Set;

public interface LikeRepository extends JpaRepository<Like, Serializable> {

    Like getLikeByUser_IdAndPost_Id(long userId, long postId);

    @Query(value = "select count(*) from social_network.likes where post_id = ?1", nativeQuery = true)
    int getPostLikes(long postId);

    @Transactional
    @Modifying
    void deleteLikesByPostId(long postId);

}
