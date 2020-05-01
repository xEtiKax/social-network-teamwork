package com.example.demo.services;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.DTO.PostDTO;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.repositories.PostRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.interfaces.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.Factory.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class PostServiceImpl_Tests {

    @Mock
    private PostRepository mockPostRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Principal principal;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private PostServiceImpl mockPostService;


    @Test(expected = EntityNotFoundException.class)
    public void getPostByIdShouldThrow_WhenPostDoesNotExist() {
        mockPostService.getPostById(anyLong());
    }

    @Test
    public void getPostByUserIdShould_CallRepository() {
        List<Post> posts = new ArrayList<>();
        User user = createUser();
        posts.add(createPost());
//        Mockito.when(mockPostRepository.findAll()).thenReturn(posts);

        //Act
        mockPostService.getPostsByUserId(user.getId());

        //Assert
        Mockito.verify(mockPostRepository,
                times(1)).getPostsByUserId(user.getId());
    }

    @Test
    public void getAllUserPostsShould_CallRepository() {
        List<Post> posts = new ArrayList<>();
        posts.add(createPost());

        mockPostService.getAllPublicPosts();

        Mockito.verify(mockPostRepository,
                times(1)).getAllPublicPosts();
    }

    @Test
    public void getMyFeedShould_CallRepository() {
        List<Post> feed = new ArrayList<>();
        feed.add(createPost());
        List<Long> friendIds = new ArrayList<>();
        friendIds.add(anyLong());

        mockPostService.getMyFeed(friendIds);

        Mockito.verify(mockPostRepository,
                times(1)).getMyFeed(friendIds);
    }

    @Test
    public void createPost_Should_CallRepository() {
        User user = createUser();
        PostDTO postDTO = createPostDTO();

        mockPostService.createPost(postDTO, user.getId());

        Mockito.verify(mockPostRepository, times(1)).save(any(Post.class));

    }

    @Test
    public void deletePostShould_CallRepository() {
        Post post = createPost();
        User user = createUser();
        Mockito.when(mockPostRepository.getById(post.getId())).thenReturn(post);
        Mockito.when(mockPostRepository.existsById(anyLong())).thenReturn(true);
        Mockito.when(mockPostRepository.save(any(Post.class))).thenReturn(post);
        post.setCreatedBy(user);

        mockPostService.deletePost(post.getId(), createUser(),true);

        Mockito.verify(mockPostRepository, times(1)).save(any(Post.class));
    }

    @Test(expected = EntityNotFoundException.class)
    public void deletePost_ShouldThrow_WhenPostDoesNotExist() {
        mockPostService.deletePost(anyLong(), createUser(),true);
    }

    @Test
    public void updatePostShould_ReturnUpdatedPost() {
        Post post = createPost();
        Mockito.when(mockPostRepository.getById(post.getId())).thenReturn(post);
        Mockito.when(mockPostRepository.existsById(anyLong())).thenReturn(true);
        Mockito.when(mockPostRepository.save(any(Post.class))).thenReturn(post);

        PostDTO updatedPost = createPostDTO();
        mockPostService.updatePost(post.getId(), updatedPost);

        Assert.assertSame(post.getText(), "text");
    }

    @Test
    public void updatePostShould_CallRepository() {
        Post post = createPost();
        Mockito.when(mockPostRepository.getById(post.getId())).thenReturn(post);
        Mockito.when(mockPostRepository.existsById(anyLong())).thenReturn(true);
        Mockito.when(mockPostRepository.save(any(Post.class))).thenReturn(post);

        PostDTO updatedPost = createPostDTO();
        mockPostService.updatePost(post.getId(), updatedPost);

        Mockito.verify(mockPostRepository, times(1)).save(any(Post.class));

    }
}
