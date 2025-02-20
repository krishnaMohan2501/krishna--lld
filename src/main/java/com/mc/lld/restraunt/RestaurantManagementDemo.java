package com.mc.lld.restraunt;

import com.mc.lld.restraunt.payment.CreditCardPayment;
import com.mc.lld.restraunt.payment.Payment;

import java.util.Arrays;
import java.util.Map;

public class RestaurantManagementDemo {

    public static void main(String[] args) {
        RestrauntManagementSystem restrauntManagementSystem = RestrauntManagementSystem.getInstance();
        User user = new User( "Divya", "M", 8318762027L, "12345");
        Restraunt restraunt = restrauntManagementSystem.registerRestaurant("1",
                Arrays.asList(new MenuItem(1, "Burger", "Delicious burger", 9.99),
                new MenuItem(3, "Salad", "Fresh salad", 7.99)));
        restraunt.addBranch("b1", "12345");

        Map<String, Map<String, MenuItem>> branchMenuItemMap = restrauntManagementSystem.searchItem("Burger", user);
        for (Map.Entry<String, Map<String, MenuItem>> entry : branchMenuItemMap.entrySet()) {
            String branchId = entry.getKey();
            Map<String, MenuItem> menuItem = entry.getValue();
            System.out.println("branchId= " + branchId);
            for (Map.Entry<String, MenuItem> item : menuItem.entrySet()) {
                System.out.println("| item name" + " - " + item.getValue().getName() + " | Description: " + item.getValue().getDescription() + " | Price: $" + item.getValue().getPrice());

            }
            System.out.println("------------------------");
        }

        Cart cart  = restrauntManagementSystem.createCart(user);
        restrauntManagementSystem.addTocart(cart, new MenuItem(1, "Burger", "Delicious burger", 9.99), 1);
        Payment payment = new CreditCardPayment();
        restrauntManagementSystem.placeOrder(cart, restraunt, payment);

        restrauntManagementSystem.rateFood(restraunt.getId(), "b1", "12345", 4, "Divya");
    }
}
