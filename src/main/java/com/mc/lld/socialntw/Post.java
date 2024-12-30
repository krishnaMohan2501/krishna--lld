package com.mc.lld.socialntw;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Post {

    private String id;
    private String userId;
    private String content;
    private List<String> imageUrls;
    private List<String> videoUrls;
    private LocalDateTime timestamp;
    private List<String> likes;
    private List<Comment> comments;

    public Post(String userId, String content) {
        this.userId = userId;
        this.content = content;
    }

}
