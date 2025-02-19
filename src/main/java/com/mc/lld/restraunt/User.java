package com.mc.lld.restraunt;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public class User {
    private String userId;
    private String name;
    private String gender;
    private Long phoneNumber;
    private String pincode;
    private List<Order> orders;

    public User(String name, String gender, Long phoneNumber, String pincode) {
        this.userId = UUID.randomUUID().toString();
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.pincode = pincode;
        this.orders = new CopyOnWriteArrayList<>();
    }

    public void addOrder(Order order) {
        orders.add(order);
    }
}
