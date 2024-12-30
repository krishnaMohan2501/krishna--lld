package com.mc.lld.coffeemach;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CoffeeVendingMachine {
    private VendingMachineState state;
    private CoffeeType selectedCoffee;
    private final Map<String, Integer> inventory;
    private final Object lock = new Object();

    public CoffeeVendingMachine() {
        this.state = new ReadyState();
        this.inventory = new ConcurrentHashMap<>();
        initializeInventory();
    }

    private void initializeInventory() {
        inventory.put("coffee", 1000);  // 1kg coffee
        inventory.put("milk", 2000);    // 2L milk
        inventory.put("foam", 1000);    // 1L foam
    }

    public void setState(VendingMachineState state) {
        this.state = state;
    }

    public void setSelectedCoffee(CoffeeType type) {
        this.selectedCoffee = type;
    }

    public CoffeeType getSelectedCoffee() {
        return selectedCoffee;
    }

    public void selectCoffee(CoffeeType type) {
        synchronized(lock) {
            state.selectCoffee(this, type);
        }
    }

    public void insertMoney(double amount) {
        synchronized(lock) {
            state.insertMoney(this, amount);
        }
    }

    public void dispense() {
        synchronized(lock) {
            state.dispense(this);
        }
    }

    public boolean hasEnoughIngredients(CoffeeType type) {
        Ingredients recipe = type.getRecipe();
        return inventory.get("coffee") >= recipe.getCoffee() &&
                inventory.get("milk") >= recipe.getMilk() &&
                inventory.get("foam") >= recipe.getFoam();
    }

    public void updateInventory(CoffeeType type) {
        Ingredients recipe = type.getRecipe();
        inventory.computeIfPresent("coffee", (k, v) -> v - recipe.getCoffee());
        inventory.computeIfPresent("milk", (k, v) -> v - recipe.getMilk());
        inventory.computeIfPresent("foam", (k, v) -> v - recipe.getFoam());

        // Check low inventory
        checkLowInventory();
    }

    private void checkLowInventory() {
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            if (entry.getValue() < 100) {
                System.out.println("Warning: Low " + entry.getKey() + " inventory!");
            }
        }
    }

    public void displayMenu() {
        System.out.println("\nAvailable Coffee Options:");
        for (CoffeeType type : CoffeeType.values()) {
            System.out.println(type.name() + " - $" + type.getPrice());
        }
        System.out.println();
    }

}
