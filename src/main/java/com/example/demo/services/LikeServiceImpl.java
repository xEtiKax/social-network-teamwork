package com.example.demo.services;

import com.example.demo.models.Like;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.repositories.LikeRepository;
import com.example.demo.repositories.PostRepository;
import com.example.demo.services.interfaces.LikeService;
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
    public void createLike(User user, Post post) {
        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        post.addLike(like);
        likeRepository.save(like);
    }

    @Override
    public void deleteLike(Like like, Post post) {
        post.removeLike(like);
        post.setLiked(false);
        likeRepository.delete(like);
    }

    @Override
    public Like getLikeByUserIdAndPostId(long userId, long postId) {
        return likeRepository.getLikeByUser_IdAndPost_Id(userId, postId);
    }

    @Override
    public boolean checkIfLikeExist(long userId, long postId) {
        return likeRepository.existsByUniquePair(userId, postId);
    }

    @Override
    public int getPostLikes(long postId) {
        return likeRepository.getPostLikes(postId);
    }

}
