package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    @Transient
    private boolean isLiked;

    @Column(name = "created_at")
    private LocalDateTime dateTime;

    @Column(name = "picture")
    private Byte[] picture;

    @Column(name = "enabled")
    private boolean enabled = true;

    @Transient
    private boolean CanDeleteUpdate;

    @JsonIgnore
    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    private Set<Like> likes = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public Post() {
    }

    public List<Comment> sortByDate(List<Comment> sortByDate) {

        Comparator<Comment> byDate = Comparator.comparing(Comment::getDateTime);

        return sortByDate.stream().sorted(byDate).collect(Collectors.toList());
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

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enable) {
        this.enabled = enable;
    }

    public Set<Like> getLikes() {
        return new HashSet<>(likes);
    }

    public Byte[] getPicture() {
        return picture;
    }

    public void setPicture(Byte[] picture) {
        this.picture = picture;
    }

    public List<Comment> getComments() {
        List<Comment> sortedComments = new ArrayList<>();
        sortedComments = sortByDate(this.comments);
        return sortedComments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public boolean getCanDeleteUpdate() {
        return CanDeleteUpdate;
    }

    public void setCanDeleteUpdate(boolean canDeleteUpdate) {
        CanDeleteUpdate = canDeleteUpdate;
    }

    public void addLike(Like like) {
        likes.add(like);
    }

    public void removeLike(Like like) {
        likes.remove(like);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
