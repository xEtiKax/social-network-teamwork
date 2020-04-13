package com.example.demo.services;

import com.example.demo.models.Like;
import com.example.demo.repositories.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {

    private LikeRepository likeRepository;

    @Autowired
    public LikeServiceImpl(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @Override
    public void createLike(Like like) {
        likeRepository.save(like);
    }

    @Override
    public void deleteLike(long likeId) {
        likeRepository.deleteById(likeId);
    }

    @Override
    public Like getLikeByUserIdAndPostId(long userId, long postId) {
        return likeRepository.getLikeByUserIdAndPostId(userId, postId);
    }

    @Override
    public int getPostLikes(long postId) {
        return likeRepository.getPostLikes(postId);
    }
}
