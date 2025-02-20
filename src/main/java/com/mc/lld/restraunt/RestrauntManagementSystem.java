package com.mc.lld.restraunt;

import com.mc.lld.restraunt.payment.Payment;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class RestrauntManagementSystem {
    private final Map<String, Restraunt> restraunt;
    private final Map<String, Cart> cartMap;
    private final Map<String, Order> orders;

    private RestrauntManagementSystem() {
        this.restraunt = new ConcurrentHashMap<>();
        this.cartMap = new ConcurrentHashMap<>();
        this.orders = new ConcurrentHashMap<>();
    }

    public static volatile RestrauntManagementSystem instance = null;

    public static RestrauntManagementSystem getInstance() {
        if (instance == null) {
            synchronized (RestrauntManagementSystem.class) {
                if (instance == null) {
                    instance = new RestrauntManagementSystem();
                }
            }
        }
        return instance;
    }

    public Restraunt registerRestaurant(String name, List<MenuItem> menuItemList) {
        Restraunt restraunt = new Restraunt(name, menuItemList);
        this.restraunt.putIfAbsent(restraunt.getId(), restraunt);
        return restraunt;
    }

    public Map<String, Map<String, MenuItem>> searchItem(String keyword, User user) {
        Map<String, Map<String, MenuItem>> result = new HashMap<>();

        for (Restraunt r : restraunt.values()) {
            // Find a branch in the user's pincode
            Branch branch = r.getBranches().stream()
                    .filter(b -> b.getPincode().equals(user.getPincode()))
                    .findFirst()
                    .orElse(null);

            if (branch != null) {
                // Find a menu item matching the keyword
                Optional<MenuItem> menuItem = r.getMenuItemList().stream()
                        .filter(item -> item.getName().equalsIgnoreCase(keyword))
                        .findFirst();

                // Add to result if a match is found

                menuItem.ifPresent(item ->
                        result.put(branch.getId(), new HashMap() {{
                            put(r.getId(), item);
                        }})
                );
                break;
            }
        }

        return result;
    }


    public void addTocart(Cart cart, MenuItem menuItem, int quantity) {
        cart.addMenuItem(menuItem, quantity);
    }

    public void placeOrder(Cart cart, Restraunt restraunt, Payment payment) {
        List<MenuItem> menuItemList = cart.getMenuItemList();
        // Find the best branch to fulfill the order
        Branch selectedBranch = selectBranchForOrder(restraunt, cart.getUser());
        if (selectedBranch == null) {
            System.out.println("No suitable branch found for the order.");
            return;
        }

        String orderId = generateOrderId();
        Order order = new Order(orderId, cart.getUser(), menuItemList, cart.getTotalCartValue(), selectedBranch);
        orders.put(orderId, order);
        cart.getUser().addOrder(order);
        payment.processPayment(order.getTotalAmount());
        if (payment.getStatus().equals(PaymentStatus.COMPLETED)) {
            order.setStatus(OrderStatus.COMPLETED);
            notifyRestaurant(restraunt, order);
        } else {
            order.setStatus(OrderStatus.CANCELLED);
        }
        clearCart(cart);
    }

    /**
     * Selects the best branch for fulfilling the order.
     * Priority: Match user's pincode -> Otherwise, select first available branch.
     */
    private Branch selectBranchForOrder(Restraunt restraunt, User user) {
        return restraunt.getBranches().stream()
                .filter(branch -> branch.getPincode().equals(user.getPincode())) // Match user's pincode
                .findFirst() // Pick the first matching branch
                .orElse(null); // Fallback to any branch
    }

    public Cart createCart(User user) {
        Cart cart = new Cart(user);
        cartMap.put(user.getUserId(), cart);
        return cart;
    }

    public void clearCart(Cart cart) {
        cartMap.remove(cart.getUser().getUserId());
        cart.setTotalCartValue(0.0);
        cart.setUser(null);
        cart.setMenuItemList(new CopyOnWriteArrayList<>());
    }

    private String generateOrderId() {
        return "ORDER" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void notifyRestaurant(Restraunt restraunt, Order order) {
        System.out.println("Notification: Order " + order.getId() + " has been placed at " + restraunt.getName() + " And branch name = " + order.getBranch().getBranchName());

        // You can enhance this by integrating a messaging system like Kafka, RabbitMQ, or an API call.
    }

    public void rateFood(String id, String branchName, String pincode, int rating, String customerName) {
        Restraunt restaurant = restraunt.get(id);
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

}
