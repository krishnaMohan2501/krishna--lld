package com.mc.lld.cab2;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
public class Reservation {
    private final String reservationId;
    private final Customer customer;
    private final Vehicle car;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private double totalPrice;
    private Station station;

    public Reservation(String reservationId, Customer customer, Vehicle car,  Station station, LocalDateTime startDate, LocalDateTime endDate) {
        this.reservationId = reservationId;
        this.customer = customer;
        this.car = car;
        this.station = station;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = calculateTotalPrice();
    }

    private double calculateTotalPrice() {
        long hoursRented = ChronoUnit.HOURS.between(startDate, endDate) + 1;
        return car.getPricePerHour() * hoursRented;
    }
}
