package com.mc.lld.parkinglot;

public class ParkingLotDemo {
    public static void run() {
        PricingStrategy pricingStrategy = new HourlyPricingStrategy(5.0);
        ParkingStrategy parkingStrategy = new NearestToExitStrategy();
        ParkingLot parkingLot = ParkingLot.getInstance(pricingStrategy, parkingStrategy);
        parkingLot.addLevel(new Level(1, 100));
        parkingLot.addLevel(new Level(2, 80));

        Vehicle car = new Car("ABC123");
        Vehicle truck = new Truck("XYZ789");
        Vehicle motorcycle = new Motorcycle("M1234");

        // Display availability
        parkingLot.displayAvailability();

        ParkingSpot carSpot = parkingLot.findNearestSpot(car);
        Ticket carTicket = parkingLot.generateTicket(car, carSpot);
        parkingLot.parkVehicle(car, carSpot);


        ParkingSpot truckSpot = parkingLot.findNearestSpot(truck);
        Ticket truckTicket = parkingLot.generateTicket(car, truckSpot);
        parkingLot.parkVehicle(truck, truckSpot);

        ParkingSpot motorcycleSpot = parkingLot.findNearestSpot(car);
        Ticket motorcycleTicket = parkingLot.generateTicket(car, motorcycleSpot);
        parkingLot.parkVehicle(motorcycle, motorcycleSpot);


        // Unpark vehicle
        parkingLot.unparkVehicle(motorcycle, motorcycleTicket);

        // Display updated availability
        parkingLot.displayAvailability();
    }
}
