package com.example.demo.services;

import com.example.demo.exceptions.AuthorizationException;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.DTO.PostDTO;
import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.repositories.PostRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.interfaces.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class PostServiceImpl implements PostService {
    public static final String POST_DOES_NOT_EXISTS = "Post does not exists.";
    private PostRepository postRepository;
    private UserRepository userRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createPost(PostDTO postDTO, long createdBy) {
        postDTO.setCreatedBy(createdBy);
        Post newPost = new Post();
        postMerge(newPost, postDTO);
        postRepository.save(newPost);
    }


    @Override
    public Post getPostById(long id) {
        throwIfPostDoesNotExists(id);
        return postRepository.getById(id);
    }

    @Override
    public List<Post> getPostsByUserId(long userId) {
        return postRepository.getPostsByUserId(userId);
    }

    @Override
    public void updatePost(long id, PostDTO postDTO) {
        throwIfPostDoesNotExists(id);
        Post postToUpdate = getPostById(id);
        postToUpdate.setText(postDTO.getText());
        postToUpdate.setIsPublic(postDTO.getIsPublic());
        postRepository.save(postToUpdate);
    }

    @Override
    public void deletePost(long id, User user, boolean isAdmin) {
        throwIfPostDoesNotExists(id);
        Post post = postRepository.getById(id);
        canUserDeleteUpdatePost(post, user, isAdmin);
        post.setEnabled(false);
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

    private void postMerge(Post post, PostDTO postDTO) {
        post.setText(postDTO.getText());
        post.setIsPublic(postDTO.getIsPublic());
        User createdBy = userRepository.getById(postDTO.getCreatedBy());
        post.setCreatedBy(createdBy);
    }

    private void throwIfPostDoesNotExists(long id) {
        if (!checkIfPostExist(id)) {
            throw new EntityNotFoundException(POST_DOES_NOT_EXISTS);
        }
    }

    private void canUserDeleteUpdatePost(Post post, User user, boolean isAdmin) {
        if (!user.getUsername().equals(post.getCreatedBy().getUsername()) && !isAdmin) {
            throw new AuthorizationException();
        }
    }

    @Override
    public List<Post> getFeedByUsersIds(User user) {
        List<Long> friendIds = getFriendIds(user);
        return postRepository.getMyFeed(friendIds);
    }

    @Override
    public List<Post> getFeedByCommonFriendsIds(User user, User me) {
        List<Long> commonFriendsIds = getCommonFriends(user,me);
        return postRepository.getMyFeed(commonFriendsIds);
    }

    private List<Long> getCommonFriends(User user, User me){
        List<Long> userFriends = getFriendIds(user);
        List<Long> myFriends = getFriendIds(me);
        List<Long> commonFriendsIds = new ArrayList<>();
        for (long id : userFriends) {
            if(myFriends.contains(id)){
                commonFriendsIds.add(id);
            }
        }
        return commonFriendsIds;
    }

    private List<Long> getFriendIds(User user) {
        List<Long> friendIds = new ArrayList<>();
        friendIds.add(user.getId());
        for (User u : user.getFriends()) {
            friendIds.add(u.getId());
        }
        return friendIds;
    }

}
