package com.mc.lld.splitwise;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Expense {

    private String id;
    private double amount;
    private String description;
    private User paidBy;
    private List<Split> splits;

    public Expense(double amount, String description, User paidBy) {
        this.id = UUID.randomUUID().toString();
        this.amount = amount;
        this.description = description;
        this.paidBy = paidBy;
        this.splits = new ArrayList<>();
    }

    public void addSplit(Split split) {
        splits.add(split);
    }


}
