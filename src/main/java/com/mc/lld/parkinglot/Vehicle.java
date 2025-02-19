package com.mc.lld.parkinglot;

import lombok.Getter;

@Getter
public abstract class Vehicle {
    protected String licenseId;
    protected VehicleType type;

    public Vehicle(String licenseId, VehicleType type) {
        this.licenseId = licenseId;
        this.type = type;
    }
}
