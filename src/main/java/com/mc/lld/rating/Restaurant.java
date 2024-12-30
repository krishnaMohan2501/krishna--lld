package com.mc.lld.rating;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public class Restaurant {

    private String name;
    private String foodItem;
    private List<Branch> branches;

    public Restaurant(String name, String foodItem) {
        this.name = name;
        this.foodItem = foodItem;
        this.branches = new CopyOnWriteArrayList<>();
    }

    public double getOverallAverageRating() {
        return branches.stream()
                .flatMap(branch -> branch.getRatings().stream())
                .mapToInt(Rating::getRating)
                .average()
                .orElse(0.0);
    }

    public void addBranch(String branchName, String pincode) {
        branches.add(new Branch(branchName, pincode));
    }
}
