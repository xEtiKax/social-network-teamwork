package com.example.demo.services;

import com.example.demo.models.Like;

public interface LikeService {
    void createLike(Like like);

    void deleteLike(Like like);

    Like getLikeByUserId(int userId);
}
