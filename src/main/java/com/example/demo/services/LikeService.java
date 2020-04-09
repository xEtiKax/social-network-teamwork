package com.example.demo.services;

import com.example.demo.models.Like;

public interface LikeService {
    void createLike(Like like);

    void deleteLike(int likeId);

    Like getLikeByUserIdAndPostId(int userId, int postId);

    int getPostLikes(int postId);
}
