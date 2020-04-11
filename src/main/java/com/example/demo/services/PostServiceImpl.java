package com.example.demo.services;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.DTO.PostDTO;
import com.example.demo.models.Post;
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
    public void createPost(PostDTO postDTO) {
        Post newPost = new Post();
        postMerge(newPost, postDTO);
        postRepository.save(newPost);
    }


    @Override
    public Post getPostById(int id) {
        return postRepository.getPostById(id);
    }

    @Override
    public List<Post> getPostsByUserId(int userId) {
        return postRepository.getPostsByUserId(userId);
    }

    @Override
    public Post updatePost(int id, PostDTO postDTO) {
        throwIfPostDoesNotExists(id);
        Post postToUpdate = getPostById(id);
        postMerge(postToUpdate, postDTO);
        postRepository.save(postToUpdate);
        return postToUpdate;
    }

    @Override
    public void deletePost(int id) {
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
    public boolean checkIfPostExist(int id) {
        return postRepository.existsById(id);
    }

    private Post postMerge(Post post, PostDTO postDTO) {
        post.setText(postDTO.getText());
        post.setIsPublic(postDTO.getIsPublic());
        return post;
//        TODO set principal
    }

    private void throwIfPostDoesNotExists(int id) {
        if (!checkIfPostExist(id)) {
            throw new EntityNotFoundException(
                    String.format(POST_DOES_NOT_EXISTS, id));
        }
    }
}
