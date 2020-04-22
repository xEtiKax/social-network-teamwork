package com.example.demo.services;

import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.exceptions.DuplicateEntityException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.Like;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.repositories.LikeRepository;
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
        if (checkLikeExists(user.getId(), post.getId())) {
            throw new DuplicateEntityException("");
        }
        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        post.addLike(like);
        likeRepository.save(like);
    }


    @Override
    public void deleteLike(Like like, Post post, User user) {
        if (!checkLikeExists(user.getId(), post.getId())) {
            throw new EntityNotFoundException("");
        }
        if (like.getUser().getUsername().equals(user.getUsername())) {
            post.removeLike(like);
            likeRepository.delete(like);
        }else{
            throw new AuthorizationException("You have no permissions to dislike post");
        }
    }

    @Override
    public boolean checkLikeExists(long userId, long potsId) {
        Like like = likeRepository.getLikeByUser_IdAndPost_Id(userId, potsId);
        return like != null;
    }


    @Override
    public Like getLikeByUserIdAndPostId(long userId, long postId) {
        return likeRepository.getLikeByUser_IdAndPost_Id(userId, postId);
    }

    @Override
    public int getPostLikes(long postId) {
        return likeRepository.getPostLikes(postId);
    }

}
