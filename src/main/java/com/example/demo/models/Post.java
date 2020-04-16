package com.example.demo.models;

import com.example.demo.models.DTO.UserDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
@Where(clause = "enabled != 0")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "is_public")
    private boolean isPublic;

    @Column(name = "created_at")
    private LocalDateTime dateTime;

    @Column(name = "picture")
    private Byte[] picture;

    @Column(name = "enabled")
    private boolean enabled = true;

    @Transient
    private boolean CanDelete;

    @JsonIgnore
    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    private Set<Like> likes = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    public Post() {
    }


    public long getId() {
        return id;
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

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enable) {
        this.enabled = enable;
    }

    public Set<Like> getLikes() {
        return new HashSet<>(likes);
    }

    public void setLikes(Set<Like> likes) {
        this.likes = likes;
    }

    public Byte[] getPicture() {
        return picture;
    }

    public void setPicture(Byte[] picture) {
        this.picture = picture;
    }

    public Set<Comment> getComments() {
        return new HashSet<>(comments);
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public boolean getCanDelete() {
        return CanDelete;
    }

    public void setCanDelete(boolean canDelete) {
        CanDelete = canDelete;
    }
}
