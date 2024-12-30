package com.mc.lld.order;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Order {

    private String id;
    private User user;
    private List<OrderItem> items;
    private double totalAmount;
    private OrderStatus status;

    public Order(String id, User user, List<OrderItem> items) {
        this.id = id;
        this.user = user;
        this.items = items;
        this.totalAmount = calculateTotalAmount();
        this.status = OrderStatus.PENDING;
    }

    private double calculateTotalAmount() {
        return items.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum();
    }

}
