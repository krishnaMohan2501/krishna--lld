package com.mc.lld.coffeemach;

// Concrete states
class ReadyState implements VendingMachineState {
    @Override
    public void selectCoffee(CoffeeVendingMachine machine, CoffeeType type) {
        if (machine.hasEnoughIngredients(type)) {
            machine.setSelectedCoffee(type);
            machine.setState(new WaitingForPaymentState());
            System.out.println("Selected " + type.name() + ". Please insert $" + type.getPrice());
        } else {
            machine.setState(new OutOfStockState());
            System.out.println("Sorry, ingredients not available for " + type.name());
        }
    }

    @Override
    public void insertMoney(CoffeeVendingMachine machine, double amount) {
        System.out.println("Please select a coffee first");
    }

    @Override
    public void dispense(CoffeeVendingMachine machine) {
        System.out.println("Please select a coffee first");
    }
}
