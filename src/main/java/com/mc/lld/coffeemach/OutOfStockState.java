package com.mc.lld.coffeemach;

class OutOfStockState implements VendingMachineState {
    @Override
    public void selectCoffee(CoffeeVendingMachine machine, CoffeeType type) {
        System.out.println("Machine out of stock, please wait");
    }

    @Override
    public void insertMoney(CoffeeVendingMachine machine, double amount) {
        System.out.println("Machine out of stock, please wait");
    }

    @Override
    public void dispense(CoffeeVendingMachine machine) {
        System.out.println("Machine out of stock, please wait");
    }
}
