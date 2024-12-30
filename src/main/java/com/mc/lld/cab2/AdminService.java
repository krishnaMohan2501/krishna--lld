package com.mc.lld.cab2;

import java.util.List;

public class AdminService {

    private final StationService stationService;
    private final VehicleService vehicleService;

    private AdminService () {
        stationService = StationService.getInstance();
        vehicleService = VehicleService.getInstance();
    }

    private static volatile AdminService INSTANCE;

    public static AdminService getInstance() {
        if (INSTANCE == null) {
            synchronized (AdminService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AdminService();
                }
            }
        }
        return INSTANCE;
    }

    // Method to onboard a new station
    public void onboardVehicleToStation(String stationId, List<Vehicle> vehicles) {
        Station station = stationService.getStation(stationId);
        for (Vehicle vehicle : vehicles) {
            station.getAvailableVehicles().add(vehicle.getVehicleId());
            station.getPrices().put(vehicle.getType(), vehicle.getPricePerHour());
            vehicleService.addVehicle(vehicle);
        }
    }

    public Station createStation(String stationName, double latitude, double longitude) {
        Station station = new Station(stationName, latitude, longitude);
        stationService.addStation(station);
        return station;
    }
}
