package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Where(clause = "enabled != 0")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "picture_id", columnDefinition = "LONGBLOB")
    private Picture photo;

    @JsonIgnore
    @Lob
    @Column(name = "cover_photo", columnDefinition = "LONGBLOB")
    private Byte[] coverPhoto;

    @Column(name = "enabled")
    private int enabled;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "age")
    private int age;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_friends",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "friend_id")})
    private Set<User> friends = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private Set<Request> requests = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Like> likes = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Comment> comments = new HashSet<>();


    public User() {
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public Byte[] getPhoto() {
//        return photo;
//    }
//
//    public void setPhoto(Byte[] photo) {
//        this.photo = photo;
//    }


    public Picture getPhoto() {
        return photo;
    }

    public void setPhoto(Picture photo) {
        this.photo = photo;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Set<User> getFriends() {
        return new HashSet<>(friends);
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    public Set<Request> getRequests() {
        return new HashSet<>(requests);
    }

    public void setRequests(Set<Request> requests) {
        this.requests = requests;
    }

    public Set<Comment> getComments() {
        return new HashSet<>(comments);
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void addRequest(Request request) {
        this.requests.add(request);
    }

    public void deleteRequest(Request request) {
        this.requests.remove(request);
    }

    public void addFriend(User user) {
        this.friends.add(user);
    }

    public void removeFriend(User user) {
        this.friends.remove(user);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Byte[] getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(Byte[] coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

}
