package com.mc.lld.splitwise;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class User {

    private String id;
    private final String name;
    private final String email;
    private Map<String, Double> balances;

    public User(String name, String email) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.balances = new ConcurrentHashMap<>();
    }

}
