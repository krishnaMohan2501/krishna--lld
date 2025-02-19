package com.mc.lld.parkinglot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ParkingLot {

    private static volatile ParkingLot instance;
    private final List<Level> levels;
    private final PricingStrategy pricingStrategy;
    private final ParkingStrategy parkingStrategy;

    private ParkingLot(PricingStrategy pricingStrategy, ParkingStrategy parkingStrategy) {
        levels = new ArrayList<>();
        this.pricingStrategy = pricingStrategy;
        this.parkingStrategy = parkingStrategy;
    }

    public static ParkingLot getInstance(PricingStrategy pricingStrategy, ParkingStrategy parkingStrategy) {
        if (instance == null) {
            synchronized (ParkingLot.class) {
                instance = new ParkingLot(pricingStrategy, parkingStrategy);
            }
        }
        return instance;
    }

    public void addLevel(Level level) {
        levels.add(level);
    }

    public void unparkVehicle(Vehicle vehicle, Ticket ticket) {
        for (Level level : levels) {
            for (ParkingSpot spot : level.getParkingSpots()) {
                if (!spot.isAvailable() && spot.getParkedVehicle().equals(vehicle)) {
                    double value = calculatePrice(ticket);
                    System.out.println("Parking fee: $" + value);
                    spot.unparkVehicle();
                    System.out.println("Successfully unParked the vehicle.");
                }
            }
        }
    }

    public synchronized void parkVehicle(Vehicle vehicle, ParkingSpot spot) {
        spot.parkVehicle(vehicle);
    }

    public void displayAvailability() {
        for (Level level : levels) {
            level.displayAvailability();
        }
    }

    public Ticket generateTicket(Vehicle vehicle, ParkingSpot spot) {
        Ticket ticket = new Ticket();
        ticket.setVehicleId(vehicle.getLicenseId());
        ticket.setParkingSpot(spot);
        ticket.setEntryTime(LocalDateTime.now());
        return ticket;
    }

    public ParkingSpot findNearestSpot(Vehicle vehicle) {
        return parkingStrategy.findNearestSpot(vehicle.getType(), levels);
    }

    private double calculatePrice(Ticket ticket) {
        return pricingStrategy.calculatePrice(ticket.getEntryTime(), LocalDateTime.now());
    }
}
