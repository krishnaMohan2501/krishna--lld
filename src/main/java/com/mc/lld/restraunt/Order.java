package com.mc.lld.restraunt;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Order {
    private final String id;
    private final List<MenuItem> items;
    private final double totalAmount;
    private OrderStatus status;
    private final LocalDateTime timestamp;
    private final User user;
    private final Branch branch;

    public Order(String id, User user, List<MenuItem> items, double totalAmount, Branch branch) {
        this.id = id;
        this.user = user;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = OrderStatus.PENDING;
        this.timestamp = LocalDateTime.now();
        this.branch = branch;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}


