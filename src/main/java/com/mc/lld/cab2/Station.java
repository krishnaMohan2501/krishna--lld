package com.mc.lld.cab2;

import lombok.Getter;
import lombok.ToString;

import java.util.*;

@Getter
@ToString
public class Station {
    private String stationId;
    private String name;
    private double latitude, longitude;
    private List<String> availableVehicles = new ArrayList<>();
    private Map<VehicleType, Double> prices = new HashMap<>();

    public Station(String name, double latitude, double longitude) {
        this.stationId = UUID.randomUUID().toString();
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
