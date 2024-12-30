package com.mc.lld.poll;

import java.util.UUID;

public class User {
    private String name;
    private String userId;

    public User(String name) {
        this.name = name;
        this.userId = UUID.randomUUID().toString();
    }
}
