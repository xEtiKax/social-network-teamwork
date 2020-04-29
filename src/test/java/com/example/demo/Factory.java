package com.example.demo;

import com.example.demo.models.*;
import com.example.demo.models.DTO.CommentDTO;
import com.example.demo.models.DTO.PostDTO;
import com.example.demo.models.DTO.UserDTO;

import java.io.IOException;

public class Factory {
    public static User createUser() {
        User user = new User();
        user.setUsername("user");
        user.setJobTitle("Developer");
        user.setEmail("user@email.com");
        user.setAge(30);
        user.setPassword("pass");
        return user;
    }

    public static UserDTO createUserDTO() {
        UserDTO user = new UserDTO();
        user.setUsername("user");
        user.setPassword("pass");
        user.setPasswordConfirmation("pass");
        return user;
    }

    public static Post createPost() {
        Post post = new Post();
        post.setId(1);
        post.setText("txt");
        post.setEnabled(true);
        return post;
    }

    public static PostDTO createPostDTO() {
        PostDTO postDTO = new PostDTO();
        postDTO.setIsPublic(true);
        postDTO.setCreatedBy(createUser().getId());
        postDTO.setText("text");
        return postDTO;
    }

    private static Byte[] multiPartToByteArr() throws IOException {
        Byte[] byteObjects = new Byte["Picture bytes".getBytes().length];
        int i = 0;
        for (byte b : "Picture bytes".getBytes()) {
            byteObjects[i++] = b;
        }
        return byteObjects;
    }

    public static Picture createPicture() throws IOException {
        Picture picture = new Picture();
        picture.setData(multiPartToByteArr());
        picture.setPublic(false);
        return picture;
    }

    public static Comment createComment() {
        Comment comment = new Comment();
        comment.setId(1);
        comment.setDescription("description");
        return comment;
    }

    public static CommentDTO createCommentDTO() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setDescription("description");
        commentDTO.setPostId(1);
        return commentDTO;
    }

    public static Like createLike() {
        Like like = new Like();
        Post post = new Post();
        User user = new User();
        like.setId(1);
        like.setPost(post);
        like.setUser(user);
        return like;
    }
}
