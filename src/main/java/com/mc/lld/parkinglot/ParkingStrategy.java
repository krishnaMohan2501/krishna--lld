package com.mc.lld.parkinglot;

import java.util.List;

public interface ParkingStrategy {
    ParkingSpot findNearestSpot(VehicleType vehicleType, List<Level> levels);
}
