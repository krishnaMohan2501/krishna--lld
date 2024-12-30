package com.mc.lld.coffeemach;

public interface VendingMachineState {
    void selectCoffee(CoffeeVendingMachine machine, CoffeeType type);
    void insertMoney(CoffeeVendingMachine machine, double amount);
    void dispense(CoffeeVendingMachine machine);
}
