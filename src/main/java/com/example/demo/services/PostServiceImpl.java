package com.example.demo.services;

import com.example.demo.models.Post;
import com.example.demo.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void createPost(Post post) {
        Post newPost = new Post();
        newPost.setText(post.getText());
        newPost.setDateTime(post.getDateTime());
        newPost.setIsPublic(post.getIsPublic());
        postRepository.save(post);
    }


    @Override
    public Post getPostById(int id) {
        return postRepository.getPostById(id);
    }

}
