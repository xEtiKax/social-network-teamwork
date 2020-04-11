package com.example.demo.services;

import com.example.demo.models.DTO.PostDTO;
import com.example.demo.models.Post;

import java.util.List;

public interface PostService {

    void createPost(PostDTO postDTO);

    Post updatePost(int id, PostDTO postDTO);

    Post getPostById(int id);

    List<Post> getPostsByUserId(int userId);

    boolean checkIfPostExist(int id);

    void deletePost(int id);

    List<Post> getAllPublicPosts();

}
