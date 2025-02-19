package com.mc.lld.parkinglot;

import java.util.List;

public class NearestToExitStrategy implements ParkingStrategy {
    @Override
    public ParkingSpot findNearestSpot(VehicleType vehicleType, List<Level> levels) {

        for (Level level : levels) {
            for (ParkingSpot spot : level.getParkingSpots()) {
                if (spot.isAvailable() && spot.getVehicleType() == vehicleType) {
                    return spot;
                }
            }
        }
        return null;
    }
}
