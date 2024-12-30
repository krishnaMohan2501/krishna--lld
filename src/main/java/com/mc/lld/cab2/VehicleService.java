package com.mc.lld.cab2;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VehicleService {

    @Getter
    private final Map<String, Vehicle> vehicleMap;

    private VehicleService() {
        this.vehicleMap = new ConcurrentHashMap<>();
    }

    private static volatile VehicleService INSTANCE;

    public static VehicleService getInstance() {
        if (INSTANCE == null) {
            synchronized (VehicleService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new VehicleService();
                }
            }
        }
        return INSTANCE;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicleMap.put(vehicle.getVehicleId(), vehicle);
    }

    public Vehicle getVehicle(String vehicleId) {
        return vehicleMap.get(vehicleId);
    }
}
