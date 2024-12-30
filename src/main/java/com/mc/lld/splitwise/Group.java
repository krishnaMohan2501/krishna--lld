package com.mc.lld.splitwise;

import lombok.Getter;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class Group {

    private String id;
    private String name;
    private List<User> members;
    private List<Expense> expenses;

    public Group(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.members = new CopyOnWriteArrayList<>();
        this.expenses = new CopyOnWriteArrayList<>();
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public void addMember(User user) {
        members.add(user);
    }


}
