package com.mc.lld.cab;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RideMatchingService {

    private final TripService tripService;
    private final DriverService driverService;

    private RideMatchingService() {
        this.tripService = TripService.getInstance();
        this.driverService = DriverService.getInstance();
    }

    private static RideMatchingService INSTANCE;

    public static RideMatchingService getInstance() {
        if (INSTANCE == null) {
            return new RideMatchingService();
        }
        return INSTANCE;
    }

    public List<Driver> getEligibleDrivers(Customer customer) {
        List<Driver> driverList = driverService.getDriverList().stream()
                .filter(driver -> driver.getAverageRating() > customer.getAverageRating()
                        && !isDriverRestricted(customer, driver) && !driver.isOffline()).collect(Collectors.toList());
        if (driverList.isEmpty()) {
            List<Trip> tempTripList = tripService.getTripList().stream().filter(trip -> trip.getCustomer().equals(customer))
                    .collect(Collectors.toList());
            Collections.reverse(tempTripList);
            driverList.add(tempTripList.stream().map(Trip::getDriver).filter(driver -> !driver.isOffline())
                    .collect(Collectors.toList()).get(0));
        }
        return driverList;
    }

    private boolean isDriverRestricted(Customer customer, Driver driver) {
        return tripService.getTripList().stream().anyMatch(trip -> (trip.getCustomer().equals(customer) && trip.getDriver().equals(driver))
                && (trip.getCustomerRating() == 1 || trip.getDriverRating() == 1));
    }
}
