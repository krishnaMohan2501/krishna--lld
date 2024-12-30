package com.mc.lld.rating;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RestaurantRatingSystem {

    private Map<String, Restaurant> restaurants;

    private RestaurantRatingSystem() {
        restaurants = new ConcurrentHashMap<>();
    }

    private static volatile RestaurantRatingSystem INSTANCE;

    public RestaurantRatingSystem getInstance() {
        if (INSTANCE == null) {
            synchronized (RestaurantRatingSystem.class) {
                return new RestaurantRatingSystem();
            }
        }
        return INSTANCE;
    }

    public void addRestaurant(String name, String foodItem) {
        restaurants.putIfAbsent(name, new Restaurant(name, foodItem));
    }

    public void addBranch(String restaurantName, String branchName, String pincode) {
        Restaurant restaurant = restaurants.get(restaurantName);
        if (restaurant != null) {
            synchronized (restaurant) {
                restaurant.addBranch(branchName, pincode);
            }
        } else {
            System.out.println("Restaurant not found.");
        }
    }

    /**
     * Search branches of restaurantName which are located at this pincode
     * @param restaurantName
     * @param pincode
     * @return
     */
    public List<Branch> searchBranches(String restaurantName, String pincode) {
        if (restaurants.containsKey(restaurantName)) {
            return restaurants.get(restaurantName).getBranches().stream()
                    .filter(branch -> branch.getPincode().equals(pincode))
                    .collect(Collectors.toList());
        }
        System.out.println("Restaurant not found.");
        return new ArrayList<>();
    }

    public void rateFood(String restaurantName, String branchName, String pincode, int rating, String customerName) {
        Restaurant restaurant = restaurants.get(restaurantName);
        if (restaurant != null) {
            for (Branch branch : restaurant.getBranches()) {
                if (branch.getBranchName().equals(branchName) && branch.getPincode().equals(pincode)) {
                    branch.addRating(rating, customerName);
                    return;
                }
            }
            System.out.println("Branch not found in the specified pincode.");
        } else {
            System.out.println("Restaurant not found.");
        }
    }

    public void showRatings(String restaurantName) {
        Restaurant restaurant = restaurants.get(restaurantName);
        if (restaurant != null) {
            System.out.println("Overall Average Rating: " + restaurant.getOverallAverageRating());
            for (Branch branch : restaurant.getBranches()) {
                System.out.println("Branch: " + branch.getBranchName() + ", Pincode: " + branch.getPincode());
                for (Rating rating : branch.getRatings()) {
                    System.out.println(rating);
                }
            }
        } else {
            System.out.println("Restaurant not found.");
        }
    }

    public void showRatingsByPincode(String restaurantName, String pincode) {
        Restaurant restaurant = restaurants.get(restaurantName);
        if (restaurant != null) {
            for (Branch branch : restaurant.getBranches()) {
                if (branch.getPincode().equals(pincode)) {
                    System.out.println("Branch: " + branch.getBranchName());
                    for (Rating rating : branch.getRatings()) {
                        System.out.println(rating);
                    }
                }
            }
        } else {
            System.out.println("Restaurant not found.");
        }
    }
}
