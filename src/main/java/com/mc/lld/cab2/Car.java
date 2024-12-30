package com.mc.lld.cab2;

import lombok.ToString;

@ToString
public class Car extends Vehicle{
    public Car(VehicleType type, double pricePerHour) {
        super(type, pricePerHour);
    }
}
