package com.mc.lld.atm;

public class AtmMain {

    // chain of responsibilty design pattern
    public static void run() {
        DollarDispenser dollarDispenser = new ValidateDollarDispenser(
                new Dollar2000Dispenser(new Dollar500Dispenser(new Dollar100Dispenser(null)))
        );

        dollarDispenser.dispense(new Currency(5600));

        dollarDispenser.dispense(new Currency(5620));
    }
}
