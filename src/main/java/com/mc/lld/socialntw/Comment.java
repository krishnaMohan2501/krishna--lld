package com.mc.lld.socialntw;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class Comment {

    private String id;
    private String userId;
    private String postId;
    private String content;
    private LocalDateTime timestamp;

    public Comment(String userId, String postId, String content) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.postId = postId;
        this.content = content;
    }
}
