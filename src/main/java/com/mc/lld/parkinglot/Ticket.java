package com.mc.lld.parkinglot;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Ticket {
    private String ticketId;
    private String vehicleId;
    private ParkingSpot parkingSpot ;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double price;
}
