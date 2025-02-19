package com.mc.lld.restraunt.payment;

import com.mc.lld.restraunt.PaymentStatus;

public abstract class Payment {
    public PaymentStatus status;

    protected Payment(PaymentStatus status) {
        this.status = status;
    }

    public abstract boolean processPayment(double amount);

    public PaymentStatus getStatus() {
        return status;
    }
}
