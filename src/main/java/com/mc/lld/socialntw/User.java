package com.mc.lld.socialntw;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private String profilePicture;
    private String bio;
    private List<String> friends;
    private List<String> posts;
    private List<String> followers;  // New field to track followers
    private List<String> following;  // New field to track following

    public User(String name, String email, String password, String profilePicture, String bio) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profilePicture = profilePicture;
        this.bio = bio;
        this.friends = new ArrayList<>();
        this.posts = new ArrayList<>();
        this.followers = new ArrayList<>();  // Initialize the followers list
        this.following = new ArrayList<>();  // Initialize the following list
    }

}
