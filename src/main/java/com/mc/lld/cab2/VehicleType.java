package com.mc.lld.cab2;

import lombok.Getter;

public enum VehicleType {
    SUV("Suv"),
    SEDAN("Sedan"),
    HATCHBACK("hatchback"),
    BIKE("Bike");

    @Getter
    private final String vehicleType;

    VehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

}
