package com.example.demo.services;

import com.example.demo.models.DTO.PostDTO;
import com.example.demo.models.Post;
import com.example.demo.models.User;

import java.security.Principal;
import java.util.List;

public interface PostService {

    void createPost(PostDTO postDTO, User createdBy);

    Post updatePost(long id, PostDTO postDTO);

    Post getPostById(long id);

    List<Post> getPostsByUserId(long userId);

    boolean checkIfPostExist(long id);

    void deletePost(long id);

    List<Post> getAllPublicPosts();

}
