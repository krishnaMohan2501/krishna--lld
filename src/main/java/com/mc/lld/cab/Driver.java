package com.mc.lld.cab;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Driver {

    private String driverName;
    private List<Integer> ratings;
    private double averageRating;
    private boolean isOffline;
    private String driverId;

    public Driver(String driverName) {
        this.driverName = driverName;
        this.driverId = UUID.randomUUID().toString();
    }
}
