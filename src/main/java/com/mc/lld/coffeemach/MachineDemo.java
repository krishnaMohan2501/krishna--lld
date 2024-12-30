package com.mc.lld.coffeemach;

public class MachineDemo {

    public static void run() {
        CoffeeVendingMachine machine = new CoffeeVendingMachine();

        // Display menu
        machine.displayMenu();

        // Simulate coffee purchase
        machine.selectCoffee(CoffeeType.LATTE);
        machine.insertMoney(5.00);

        machine.selectCoffee(CoffeeType.ESPRESSO);
        machine.insertMoney(3.00);

        machine.selectCoffee(CoffeeType.CAPPUCCINO);
        machine.insertMoney(4.00);


    }
}
