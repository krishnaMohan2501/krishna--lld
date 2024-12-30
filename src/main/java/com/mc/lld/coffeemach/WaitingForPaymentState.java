package com.mc.lld.coffeemach;

class WaitingForPaymentState implements VendingMachineState {
    @Override
    public void selectCoffee(CoffeeVendingMachine machine, CoffeeType type) {
        System.out.println("Please complete current transaction first");
    }

    @Override
    public void insertMoney(CoffeeVendingMachine machine, double amount) {
        CoffeeType selectedCoffee = machine.getSelectedCoffee();
        if (amount >= selectedCoffee.getPrice()) {
            double change = amount - selectedCoffee.getPrice();
            machine.setState(new DispensingState());
            System.out.println("Payment accepted. Change: $" + change);
            machine.dispense();
        } else {
            System.out.println("Insufficient amount. Please insert $" +
                    (selectedCoffee.getPrice() - amount) + " more");
        }
    }

    @Override
    public void dispense(CoffeeVendingMachine machine) {
        System.out.println("Please insert money first");
    }
}
