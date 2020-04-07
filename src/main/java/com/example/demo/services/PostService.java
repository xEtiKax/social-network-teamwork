package com.example.demo.services;

import com.example.demo.models.Post;

import java.util.List;

public interface PostService {

    void createPost(Post post);

    Post getPostById(int id);

}
