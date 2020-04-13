package com.example.demo.services;

import com.example.demo.models.Like;

public interface LikeService {
    void createLike(Like like);

    void deleteLike(long likeId);

    Like getLikeByUserIdAndPostId(long userId, long postId);

    int getPostLikes(long postId);
}
