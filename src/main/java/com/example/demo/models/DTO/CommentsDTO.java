package com.example.demo.models.DTO;

public class CommentsDTO {

    private String description;

    private int postId;

    public CommentsDTO() {
    }

    public CommentsDTO(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
