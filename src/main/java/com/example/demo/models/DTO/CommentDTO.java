package com.example.demo.models.DTO;

public class CommentDTO {

    private String description;

    private long postId;

    public CommentDTO() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }
}
