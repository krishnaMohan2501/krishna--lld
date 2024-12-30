package com.mc.lld.coffeemach;

class Ingredients {
    private int coffee; // in grams
    private int milk;   // in ml
    private int foam;   // in ml

    public Ingredients(int coffee, int milk, int foam) {
        this.coffee = coffee;
        this.milk = milk;
        this.foam = foam;
    }

    // Getters and setters
    public int getCoffee() { return coffee; }
    public int getMilk() { return milk; }
    public int getFoam() { return foam; }
}

