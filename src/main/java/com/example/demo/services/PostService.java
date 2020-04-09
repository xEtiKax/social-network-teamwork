package com.example.demo.services;

import com.example.demo.models.Post;

public interface PostService {

    void createPost(Post post);

    Post updatePost(int id, Post post);

    Post getPostById(int id);

    void deletePost(int id);

    boolean checkIfPostExist(int id);


}
