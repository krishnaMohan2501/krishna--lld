package com.mc.lld.cab2;

import java.util.Arrays;

public class VehicleRentalServiceDemo {
    public static void main(String[] args) {
        VehicleRentalService vehicleRentalService = VehicleRentalService.getInstance();

        AdminService adminService = AdminService.getInstance();

        Station station1 = adminService.createStation("Station1", 12.9716, 77.5946);
        Station station2 = adminService.createStation("Station2", 13.0827, 80.2707);

        Car car1 = new Car(VehicleType.SUV, 11);
        Car car2 = new Car(VehicleType.SEDAN, 12);
        Car car3 = new Car(VehicleType.BIKE, 5);
        Car car4 = new Car(VehicleType.SUV, 10);
        Car car5 = new Car(VehicleType.HATCHBACK, 8);

        adminService.onboardVehicleToStation(station1.getStationId(), Arrays.asList(
                car1,
                car2,
                car3
        ));

        adminService.onboardVehicleToStation(station2.getStationId(), Arrays.asList(
                car4,
                car5
        ));




    }
}
