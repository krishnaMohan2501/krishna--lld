package com.mc.lld.poll;

import java.time.LocalDateTime;
import java.util.UUID;

public class Vote {

    private String voteId;
    private String userId;
    private String pollId;
    private String response;
    private LocalDateTime time;

    public Vote(String userId, String pollId, String response) {
        this.userId = userId;
        this.pollId = pollId;
        this.response = response;
        this.voteId = UUID.randomUUID().toString();
        this.time = LocalDateTime.now();
    }

}
