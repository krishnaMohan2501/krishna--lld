package com.mc.lld.rating;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public class Branch {

    private String branchName;
    private String pincode;
    private List<Rating> ratings;

    public Branch(String branchName, String pincode) {
        this.branchName = branchName;
        this.pincode = pincode;
        this.ratings = new CopyOnWriteArrayList<>();
    }

    public void addRating(int rating, String customerName) {
        ratings.add(new Rating(rating, customerName));
    }

    public double getAverageRating() {
        return ratings.stream().mapToInt(Rating::getRating).average().orElse(0.0);
    }
}
