package com.mc.lld.restraunt;

import lombok.Data;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class Cart {
    public User user;
    private List<MenuItem> menuItemList;
    public Double totalCartValue;

    public Cart(User user) {
        this.user = user;
        this.menuItemList = new CopyOnWriteArrayList<>();
        this.totalCartValue = 0.0;
    }

    public void addMenuItem(MenuItem menuItem, int quantity) {
        menuItemList.add(menuItem);
        this.totalCartValue = menuItemList.stream().mapToDouble(x -> x.getPrice() * quantity).sum();
    }
}
