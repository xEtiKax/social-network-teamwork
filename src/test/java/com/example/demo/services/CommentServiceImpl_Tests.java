package com.example.demo.services;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.Comment;
import com.example.demo.models.DTO.CommentDTO;
import com.example.demo.models.DTO.PostDTO;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.services.interfaces.CommentService;
import com.example.demo.services.interfaces.PostService;
import com.example.demo.services.interfaces.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.Factory.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class CommentServiceImpl_Tests {

    @Mock
    private CommentRepository mockCommentRepository;

    @Mock
    private UserService mockUserService;

    @Mock
    private PostService postService;

    @InjectMocks
    private CommentServiceImpl mockCommentServiceImpl;

    @Test(expected = EntityNotFoundException.class)
    public void getCommentByIdShouldThrow_WhenCommentDoesNotExist() {
        mockCommentServiceImpl.getById(anyLong());
    }

    @Test
    public void getCommentsByPostIdShould_CallRepository() {
        List<Comment> comments = new ArrayList<>();
        Post post = createPost();
        comments.add(createComment());

        //Act
        mockCommentServiceImpl.getCommentsByPostId(post.getId());

        //Assert
        Mockito.verify(mockCommentRepository,
                times(1)).getCommentsByPostId(post.getId());
    }

    @Test
    public void createComment_Should_CallRepository() {

        mockCommentServiceImpl.createComment(createComment());

        Mockito.verify(mockCommentRepository, times(1)).save(any(Comment.class));

    }

    @Test
    public void getCommentByUserIdShould_CallRepository() {
        List<Comment> comments = new ArrayList<>();
        User user = createUser();
        comments.add(createComment());

        //Act
        mockCommentServiceImpl.getCommentsByUserId(user.getId());

        //Assert
        Mockito.verify(mockCommentRepository,
                times(1)).getCommentsByUserId(user.getId());
    }

    @Test
    public void updateCommentShould_CallRepository() {
        Comment comment = createComment();
        Mockito.when(mockCommentRepository.getByCommentId(comment.getId())).thenReturn(comment);
        Mockito.when(mockCommentRepository.existsById(anyLong())).thenReturn(true);
        Mockito.when(mockCommentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDTO updatedComment = createCommentDTO();
        mockCommentServiceImpl.updateComment(comment.getId(), updatedComment);

        Mockito.verify(mockCommentRepository, times(1)).save(any(Comment.class));

    }

    @Test
    public void updateCommentShould_ReturnUpdatedComment() {
        Comment comment = createComment();
        Mockito.when(mockCommentRepository.getByCommentId(comment.getId())).thenReturn(comment);
        Mockito.when(mockCommentRepository.existsById(anyLong())).thenReturn(true);
        Mockito.when(mockCommentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDTO updatedComment = createCommentDTO();
        mockCommentServiceImpl.updateComment(comment.getId(), updatedComment);

        Assert.assertSame(comment.getDescription(), "description");
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteComment_ShouldThrow_WhenCommentDoesNotExist() {
        mockCommentServiceImpl.deleteComment(anyLong(), createUser().getUsername());
    }

//    @Test
//    public void deleteCommentShould_CallRepository() {
//        Comment comment = createComment();
//        User user = createUser();
//        Post post = createPost();
//        Mockito.when(mockCommentRepository.getByCommentId(comment.getId())).thenReturn(comment);
//        Mockito.when(mockCommentRepository.existsById(anyLong())).thenReturn(true);
//        Mockito.when(mockCommentRepository.save(any(Comment.class))).thenReturn(comment);
//        comment.setUser(user);
//        comment.setPost(post);
//
//        mockCommentServiceImpl.deleteComment(comment.getId(), createUser().getUsername());
//
//        Mockito.verify(mockCommentRepository, times(1)).save(any(Comment.class));
//    }
}
