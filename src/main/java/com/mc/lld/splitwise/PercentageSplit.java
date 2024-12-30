package com.mc.lld.splitwise;

public class PercentageSplit extends Split {

    private final double percent;

    public PercentageSplit(User user, double percent) {
        super(user);
        this.percent = percent;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    public double getPercent() {
        return percent;
    }
}
