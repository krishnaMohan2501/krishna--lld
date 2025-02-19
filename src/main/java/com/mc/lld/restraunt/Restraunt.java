package com.mc.lld.restraunt;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class Restraunt {
    private final String id;
    private final String name;
    private final List<MenuItem> menuItemList;
    private List<Branch> branches;

    public Restraunt(String name, List<MenuItem> menuItemList) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.menuItemList = menuItemList;
        this.branches = new CopyOnWriteArrayList<>();
    }

    public void addBranch(String branchName, String pincode) {
        Branch branch = new Branch(branchName, pincode, this); // Pass this restaurant as reference
        branches.add(branch);
    }

}
