package com.mc.lld.restraunt;

import com.mc.lld.restraunt.payment.CreditCardPayment;
import com.mc.lld.restraunt.payment.Payment;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RestaurantManagementDemo {

    public static void main(String[] args) {
        RestrauntManagementSystem restrauntManagementSystem = RestrauntManagementSystem.getInstance();
        User user = new User( "Divya", "M", 8318762027L, "12345");
        Restraunt restraunt = restrauntManagementSystem.registerRestaurant("1",
                Arrays.asList(new MenuItem(1, "Burger", "Delicious burger", 9.99),
                new MenuItem(3, "Salad", "Fresh salad", 7.99)));
        restraunt.addBranch("b1", "12345");

        Map<Branch, MenuItem> branchMenuItemMap= restrauntManagementSystem.searchItem("Burger", user);
        for (Map.Entry<Branch, MenuItem> entry : branchMenuItemMap.entrySet()) {
            Branch branch = entry.getKey();
            MenuItem menuItem = entry.getValue();

            System.out.println("Restaurant ID = " + branch.getRestaurant().getId());
            System.out.println(" - " + menuItem.getName() + " | Description: " + menuItem.getDescription() + " | Price: $" + menuItem.getPrice());
            System.out.println("------------------------");
        }

        Cart cart  = restrauntManagementSystem.createCart(user);
        restrauntManagementSystem.addTocart(cart, new MenuItem(1, "Burger", "Delicious burger", 9.99), 1);
        Payment payment = new CreditCardPayment();
        restrauntManagementSystem.placeOrder(cart, restraunt, payment);
    }
}
