package com.mc.lld.sprintplanner.model;

import lombok.Getter;

import java.util.UUID;

@Getter
public class User {
    private String name;
    private String id;

    public User(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }
}
