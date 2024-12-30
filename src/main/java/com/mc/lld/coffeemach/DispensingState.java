package com.mc.lld.coffeemach;

class DispensingState implements VendingMachineState {
    @Override
    public void selectCoffee(CoffeeVendingMachine machine, CoffeeType type) {
        System.out.println("Please wait, preparing coffee");
    }

    @Override
    public void insertMoney(CoffeeVendingMachine machine, double amount) {
        System.out.println("Please wait, preparing coffee");
    }

    @Override
    public void dispense(CoffeeVendingMachine machine) {
        CoffeeType selectedCoffee = machine.getSelectedCoffee();
        machine.updateInventory(selectedCoffee);
        System.out.println("Dispensing " + selectedCoffee.name() + "... Enjoy!");
        machine.setState(new ReadyState());
    }
}

