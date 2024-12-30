package com.mc.lld.coffeemach;

enum CoffeeType {
    ESPRESSO(2.50, new Ingredients(20, 0, 0)),
    CAPPUCCINO(3.50, new Ingredients(20, 50, 0)),
    LATTE(4.00, new Ingredients(20, 70, 30));

    private final double price;
    private final Ingredients recipe;

    CoffeeType(double price, Ingredients recipe) {
        this.price = price;
        this.recipe = recipe;
    }

    public double getPrice() {
        return price;
    }

    public Ingredients getRecipe() {
        return recipe;
    }
}

