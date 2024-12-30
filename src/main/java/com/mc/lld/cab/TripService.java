package com.mc.lld.cab;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class TripService {

    @Getter
    private final List<Trip> tripList = new ArrayList<>();

    private static TripService INSTANCE;
    private TripService () {

    }

    public static TripService getInstance() {
        if (INSTANCE == null) {
            return new TripService();
        }
        return INSTANCE;
    }

    public void registerTrip(Trip trip) {
        tripList.add(trip);
    }
}
