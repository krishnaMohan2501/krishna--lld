package com.mc.lld.order;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class User {

    private String id;
    private String name;
    private String email;
    private String password;
    private List<Order> orders;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;

    }

    public void addOrder(Order order) {
        orders.add(order);
    }

}
