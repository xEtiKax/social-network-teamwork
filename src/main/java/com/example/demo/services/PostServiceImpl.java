package com.example.demo.services;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.DTO.PostDTO;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.repositories.PostRepository;
import com.example.demo.services.interfaces.PostService;
import com.example.demo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;


@Service
public class PostServiceImpl implements PostService {
    public static final String POST_DOES_NOT_EXISTS = "Post does not exists.";
    private PostRepository postRepository;
    private UserService userService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Override
    public void createPost(PostDTO postDTO, long createdBy) {
        postDTO.setCreatedBy(createdBy);
        Post newPost = new Post();
        postMerge(newPost, postDTO);
        postRepository.save(newPost);
    }


    @Override
    public Post getPostById(long id) {
        return postRepository.getById(id);
    }

    @Override
    public List<Post> getPostsByUserId(long userId) {
        return postRepository.getPostsByUserId(userId);
    }

    @Override
    public void updatePost(long id, PostDTO postDTO) {
        throwIfPostDoesNotExists(id);
        Post postToUpdate = getPostById(id);
        postToUpdate.setText(postDTO.getText());
        postToUpdate.setIsPublic(postDTO.getIsPublic());
        postRepository.save(postToUpdate);
    }

    @Override
    public void deletePost(long id, Principal principal, HttpServletRequest request) {
        throwIfPostDoesNotExists(id);
        Post post = postRepository.getById(id);
        canUserDeleteUpdatePost(post, principal, request);
        post.setEnabled(false);
        postRepository.save(post);
    }

    @Override
    public List<Post> getAllPublicPosts() {
        return postRepository.getAllPublicPosts();
    }

    @Override
    public boolean checkIfPostExist(long id) {
        return postRepository.existsById(id);
    }

    private void postMerge(Post post, PostDTO postDTO) {
        post.setText(postDTO.getText());
        post.setIsPublic(postDTO.getIsPublic());
        User createdBy = userService.getById(postDTO.getCreatedBy());
        post.setCreatedBy(createdBy);
    }

    private void throwIfPostDoesNotExists(long id) {
        if (!checkIfPostExist(id)) {
            throw new EntityNotFoundException(
                    String.format(POST_DOES_NOT_EXISTS, id));
        }
    }

    private void canUserDeleteUpdatePost(Post post, Principal principal, HttpServletRequest request) {
        if (principal != null && (principal.getName().equals(post.getCreatedBy().getUsername()) ||
                request.isUserInRole("ROLE_ADMIN"))) {
            post.setCanDeleteUpdate(true);
        }
        post.setCanDeleteUpdate(false);
    }


    @Override
    public List<Post> getMyFeed(List<Long> friendIds) {
        return postRepository.getMyFeed(friendIds);
    }
}
