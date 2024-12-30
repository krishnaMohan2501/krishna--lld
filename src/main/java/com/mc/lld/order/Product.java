package com.mc.lld.order;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Product {

    private String id;
    private String name;
    private String description;
    private double price;
    private int quantity;


    public Product(String name, String description, double price, int quantity) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public synchronized void updateQuantity(int quantity) {
        this.quantity += quantity;
    }

    public synchronized boolean isAvailable(int quantity) {
        return this.quantity >= quantity;
    }




}
