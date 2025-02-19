package com.mc.lld.parkinglot;

import java.time.Duration;
import java.time.LocalDateTime;

public class HourlyPricingStrategy implements PricingStrategy {
    private double hourlyRate;

    public HourlyPricingStrategy(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @Override
    public double calculatePrice(LocalDateTime entryTime, LocalDateTime exitTime) {
        long duration = Duration.between(entryTime, exitTime).toHours();
        return duration * hourlyRate;
    }
}
