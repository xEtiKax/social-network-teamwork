package com.example.demo.services.interfaces;

import com.example.demo.models.Like;
import com.example.demo.models.Post;
import com.example.demo.models.User;


public interface LikeService {
    void createLike(User user, Post post);

    void deleteLike(Like like, Post post);

    Like getLikeByUserIdAndPostId(long userId, long postId);

    int getPostLikes(long postId);

    boolean checkIfLikeExist(long userId, long postId);
}
