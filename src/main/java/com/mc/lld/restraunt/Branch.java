package com.mc.lld.restraunt;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public class Branch {

    private String branchName;
    private String pincode;
    private List<Rating> ratings;
    private final Restraunt restaurant; // Reference to the parent restaurant

    public Branch(String branchName, String pincode, Restraunt restaurant) {
        this.branchName = branchName;
        this.pincode = pincode;
        this.ratings = new CopyOnWriteArrayList<>();
        this.restaurant = restaurant;
    }

    public void addRating(int rating, String customerName) {
        ratings.add(new Rating(rating, customerName));
    }

    public double getAverageRating() {
        return ratings.stream().mapToInt(Rating::getRating).average().orElse(0.0);
    }
}
