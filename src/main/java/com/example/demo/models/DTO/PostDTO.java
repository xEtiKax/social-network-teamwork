package com.example.demo.models.DTO;

import com.example.demo.models.Comments;
import com.example.demo.models.Like;
import com.example.demo.models.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class PostDTO {
    private String text;
    private User createdBy;
    private boolean isPublic;
    private LocalDateTime dateTime;
    private Set<Like> likes = new HashSet<>();
    private Set<Comments> comments = new HashSet<>();

    public PostDTO() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Set<Like> getLikes() {
        return likes;
    }

    public void setLikes(Set<Like> likes) {
        this.likes = likes;
    }

    public Set<Comments> getComments() {
        return comments;
    }

    public void setComments(Set<Comments> comments) {
        this.comments = comments;
    }
}
