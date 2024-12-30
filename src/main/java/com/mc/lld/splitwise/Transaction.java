package com.mc.lld.splitwise;

public class Transaction {

    private String id;
    private User sender;
    private User receiver;
    private double amount;

    public Transaction(String id, User sender, User receiver, double amount) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

}
