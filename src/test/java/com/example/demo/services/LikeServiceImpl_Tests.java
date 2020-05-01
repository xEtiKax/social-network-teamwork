package com.example.demo.services;

import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.exceptions.DuplicateEntityException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.DTO.PostDTO;
import com.example.demo.models.Like;
import com.example.demo.models.Post;
import com.example.demo.models.Request;
import com.example.demo.models.User;
import com.example.demo.repositories.LikeRepository;
import com.example.demo.repositories.PostRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.Factory.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class LikeServiceImpl_Tests {

    @Mock
    private LikeRepository mockLikeRepository;

    @InjectMocks
    private LikeServiceImpl mockLikeServiceImpl;

    @Test
    public void createLike_Should_CallRepository() {
        User user = createUser();
        Post post = createPost();

        mockLikeServiceImpl.createLike(user, post);

        Mockito.verify(mockLikeRepository, times(1)).save(any(Like.class));

    }

    @Test(expected = DuplicateEntityException.class)
    public void createLike_ShouldThrow_When_AlreadyLiked() {
        User user = createUser();
        Post post = createPost();
        Like like = createLike();
        Mockito.when(mockLikeRepository.getLikeByUser_IdAndPost_Id(user.getId(), post.getId())).thenReturn(like);

        mockLikeServiceImpl.createLike(user, post);
        mockLikeServiceImpl.createLike(user, post);

    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteLike_ShouldThrow_WhenLikeDoesNotExist() {
        mockLikeServiceImpl.deleteLike(createLike(), createPost(), createUser());
    }

    @Test
    public void deleteLikeShould_CallRepository() {
        Post post = createPost();
        User user = createUser();
        Like like = createLike();
        Mockito.when(mockLikeRepository.getLikeByUser_IdAndPost_Id(user.getId(), post.getId())).thenReturn(like);
        post.addLike(like);
        like.setUser(user);
        like.setPost(post);

        mockLikeServiceImpl.deleteLike(like, post, createUser());
    }

    @Test(expected = AuthorizationException.class)
    public void deleteLikeShould_Throw_When_WrongCreator() {
        Post post = createPost();
        User user = createUser();
        User user2 = createUser();
        user2.setUsername("gosho");
        Like like = createLike();
        Mockito.when(mockLikeRepository.getLikeByUser_IdAndPost_Id(user.getId(), post.getId())).thenReturn(like);
        post.addLike(like);
        like.setUser(user2);
        like.setPost(post);

        mockLikeServiceImpl.deleteLike(like, post, user);

        Mockito.verify(mockLikeRepository, times(1)).getLikeByUser_IdAndPost_Id(user.getId(), post.getId());
    }

    @Test
    public void getLikeByUserAndPostIdShould_CallRepository() {
        Post post = createPost();
        User user = createUser();

        mockLikeServiceImpl.getLikeByUserIdAndPostId(user.getId(), post.getId());

        Mockito.verify(mockLikeRepository,
                times(1)).getLikeByUser_IdAndPost_Id(user.getId(), post.getId());
    }

    @Test
    public void getPostLikesShould_CallRepository() {
        List<Like> likes = new ArrayList<>();
        Post post = createPost();
        likes.add(createLike());

        mockLikeServiceImpl.getPostLikes(post.getId());

        Mockito.verify(mockLikeRepository,
                times(1)).getPostLikes(post.getId());
    }

}
