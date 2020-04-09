package com.example.demo.services;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.Post;
import com.example.demo.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {
    public static final String POST_WITH_ID_DOES_NOT_EXISTS = "Post with %d does not exists.";
    private PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void createPost(Post post) {
        Post newPost = new Post();
        postMerge(newPost, post);
        postRepository.save(post);
    }


    @Override
    public Post getPostById(int id) {
        return postRepository.getPostById(id);
    }

    @Override
    public Post updatePost(int id, Post post) {
        throwIfPostDoesNotExists(id);
        Post postToUpdate = getPostById(id);
        postMerge(postToUpdate, post);
        postRepository.save(postToUpdate);
        return postToUpdate;
    }

    @Override
    public void deletePost(int id) {
        throwIfPostDoesNotExists(id);
        postRepository.deleteById(id);
    }

    @Override
    public boolean checkIfPostExist(int id) {
        return postRepository.existsById(id);
    }

    private Post postMerge(Post post, Post newPost) {
        post.setText(newPost.getText());
        post.setDateTime(newPost.getDateTime());
        post.setIsPublic(newPost.getIsPublic());
        return post;
    }

    private void throwIfPostDoesNotExists(int id) {
        if (!checkIfPostExist(id)) {
            throw new EntityNotFoundException(
                    String.format(POST_WITH_ID_DOES_NOT_EXISTS, id));
        }
    }
}
