package com.example.demo.services.interfaces;

import com.example.demo.models.DTO.PostDTO;
import com.example.demo.models.Post;
import com.example.demo.models.User;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

public interface PostService {

    void createPost(PostDTO postDTO, long createdBy);

    void updatePost(long id, PostDTO postDTO);

    Post getPostById(long id);

    List<Post> getPostsByUserId(long userId);

    boolean checkIfPostExist(long id);

    void deletePost(long id, User user);

    List<Post> getAllPublicPosts();

    List<Post> getMyFeed(List<Long> friendIds);

}
