package com.mc.lld.parkinglot;

import java.time.LocalDateTime;

public interface PricingStrategy {
    double calculatePrice(LocalDateTime entryTime, LocalDateTime exitTime);
}