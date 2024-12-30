package com.mc.lld.socialntw;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class Notification {

    private String id;
    private String userId;
    private NotificationType type;
    private String content;
    private LocalDateTime timestamp;

    public Notification(String userId, NotificationType type, String content) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.type = type;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }
}
