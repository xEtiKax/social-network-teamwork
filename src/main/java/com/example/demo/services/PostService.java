package com.example.demo.services;

import com.example.demo.models.DTO.PostDTO;
import com.example.demo.models.Post;

public interface PostService {

    void createPost(PostDTO postDTO);

    Post updatePost(int id, PostDTO postDTO);

    Post getPostById(int id);

    void deletePost(int id);

    boolean checkIfPostExist(int id);


}
