package com.example.demo.models.DTO;

public class CommentsDTO {

    private String description;

    private long postId;

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


    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }
}
