package com.mc.lld.cab;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Customer {

    private String customerName;
    private double averageRating;
    private List<Integer> ratings;
    private String customerId;

    public Customer(String customerName) {
        this.customerName = customerName;
        this.customerId = UUID.randomUUID().toString();
    }
}
