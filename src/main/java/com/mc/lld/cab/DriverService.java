package com.mc.lld.cab;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class DriverService {

    @Getter
    private final List<Driver> DriverList = new ArrayList<>();

    private static DriverService INSTANCE;
    private DriverService () {

    }

    public static DriverService getInstance() {
        if (INSTANCE == null) {
            return new DriverService();
        }
        return INSTANCE;
    }

    public void registerDriver(Driver driver) {
        // Register Drivers
        DriverList.add(driver);
    }


}
