package com.example.demo.services;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.DTO.PostDTO;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    public static final String POST_DOES_NOT_EXISTS = "Post does not exists.";
    private PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void createPost(PostDTO postDTO, User createdBy) {
        postDTO.setCreatedBy(createdBy);
        Post newPost = new Post();
        postMerge(newPost, postDTO);
        postRepository.save(newPost);
    }


    @Override
    public Post getPostById(long id) {
        return postRepository.getPostById(id);
    }

    @Override
    public List<Post> getPostsByUserId(long userId) {
        return postRepository.getPostsByUserId(userId);
    }

    @Override
    public Post updatePost(long id, PostDTO postDTO) {
        throwIfPostDoesNotExists(id);
        Post postToUpdate = getPostById(id);
        postMerge(postToUpdate, postDTO);
        postRepository.save(postToUpdate);
        return postToUpdate;
    }

    @Override
    public void deletePost(long id) {
        throwIfPostDoesNotExists(id);
        Post post = postRepository.getPostById(id);
        post.setEnabled(0);
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

    private Post postMerge(Post post, PostDTO postDTO) {
        post.setText(postDTO.getText());
        post.setIsPublic(postDTO.getIsPublic());
        post.setCreatedBy(postDTO.getCreatedBy());
        return post;
    }

    private void throwIfPostDoesNotExists(long id) {
        if (!checkIfPostExist(id)) {
            throw new EntityNotFoundException(
                    String.format(POST_DOES_NOT_EXISTS, id));
        }
    }
}
