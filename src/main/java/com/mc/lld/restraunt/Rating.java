package com.mc.lld.restraunt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rating {
    private int rating;
    private String comment;

    public Rating(int rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }
}
