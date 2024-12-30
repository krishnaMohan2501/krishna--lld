package com.mc.lld.cab;

import com.mc.lld.cab.Customer;
import com.mc.lld.cab.Driver;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Trip {

    private Customer customer;
    private Driver driver;
    int customerRating;
    int driverRating;
}
