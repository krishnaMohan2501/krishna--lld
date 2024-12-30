package com.mc.lld.cab2;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public abstract class Vehicle {
    @Getter
    private String vehicleId;
    @Getter
    private VehicleType type;
    @Getter
    private double pricePerHour;

    @Getter
    @Setter
    private boolean available;

    protected Vehicle(VehicleType type, double pricePerHour) {
        this.vehicleId = UUID.randomUUID().toString();
        this.type = type;
        this.pricePerHour = pricePerHour;
        this.available = true;
    }
}
