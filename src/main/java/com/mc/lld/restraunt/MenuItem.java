package com.mc.lld.restraunt;

import lombok.Getter;

@Getter
public class MenuItem {

    private final int id;
    private final String name;
    private final String description;
    private final double price;

    public MenuItem(int id, String name, String description, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
