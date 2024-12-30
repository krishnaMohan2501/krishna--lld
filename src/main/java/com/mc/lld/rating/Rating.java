package com.mc.lld.rating;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rating {

    private int rating;
    private String customerName;

    public Rating(int rating, String customerName) {
        this.rating = rating;
        this.customerName = customerName;
    }
}
