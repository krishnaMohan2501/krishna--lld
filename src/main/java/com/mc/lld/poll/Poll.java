package com.mc.lld.poll;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public class Poll {

    private String pollId;
    private String question;
    private List<String> options;
    private LocalDateTime createdAt;

    public Poll(String question) {
        this.pollId = UUID.randomUUID().toString();
        this.question = question;
        this.options = new CopyOnWriteArrayList<>();
        this.createdAt = LocalDateTime.now();
    }
}