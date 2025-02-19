package com.mc.lld.restraunt.payment;


import com.mc.lld.restraunt.PaymentStatus;

public class CreditCardPayment extends Payment {

    public CreditCardPayment() {
        super(PaymentStatus.PENDING);
    }

    @Override
    public boolean processPayment(double amount) {
        // Simulating payment processing logic
        boolean isSuccess = processCreditCard(amount);

        if (isSuccess) {
            this.status = PaymentStatus.COMPLETED;
        } else {
            this.status = PaymentStatus.FAILED;
        }

        return isSuccess;
    }

    private boolean processCreditCard(double amount) {
        // Actual credit card processing logic (mocked here)
        System.out.println("Processing credit card payment of $" + amount);
        return true; // Assume payment is successful
    }
}

