package com.mc.lld.cab2;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class VehicleRentalService {

    private final Map<String, Reservation> reservations = new ConcurrentHashMap<>();
    // Map of vehicle ID -> Station Name
    private final StationService stationService;
    private final VehicleService vehicleService;

    private VehicleRentalService () {
        stationService = StationService.getInstance();
        vehicleService = VehicleService.getInstance();
    }

    private static volatile VehicleRentalService INSTANCE;

    public static VehicleRentalService getInstance() {
        if (INSTANCE == null) {
            synchronized (VehicleRentalService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new VehicleRentalService();
                }
            }
        }
        return INSTANCE;
    }

    // Method to drop a vehicle
    public String dropVehicle(String vehicleId, String stationId) {
        Station station = stationService.getStation(stationId);
        if (station == null) {
            return "Invalid station.";
        }
        Vehicle vehicle = vehicleService.getVehicleMap().get(vehicleId);
        vehicle.setAvailable(true);
        station.getAvailableVehicles().add(vehicleId);
        return "Vehicle dropped successfully at " + station.getName();
    }

    // Method to book a vehicle
    public synchronized void bookVehicle(Customer customer, Station station, String vehicleId) {
        if (station == null) {
            System.out.println("Vehicle not available.");
            return;
        }
        Vehicle vehicle = vehicleService.getVehicleMap().get(vehicleId);
        makeReservation(station, customer, vehicle, LocalDateTime.now(),  LocalDateTime.now().plusDays(3));
        System.out.println("Vehicle booked successfully. Vehicle ID: " + vehicleId);
    }

    public synchronized Reservation makeReservation(Station station, Customer customer, Vehicle car, LocalDateTime startDate, LocalDateTime endDate) {
        if (isCarAvailable(car, startDate, endDate)) {
            String reservationId = generateReservationId();
            Reservation reservation = new Reservation(reservationId, customer, car, station, startDate, endDate);
            reservations.put(reservationId, reservation);
            car.setAvailable(false);
            station.getAvailableVehicles().remove(car.getVehicleId());
            return reservation;
        }
        return null;
    }

    // Method to generate station report
    public void generateStationReport(String stationId) {
        Map<String, Map<VehicleType, List<Vehicle>>> stationVehicleCountMap = new HashMap<>();

        Station station = stationService.getStation(stationId);
        if (station == null) {
            System.out.println("Station not found.");
            return;
        }

        System.out.println("Station Report for " + station.getName() + ":\n");
        List<String> vehicleIds = station.getAvailableVehicles();
        for (String availableVehicle : vehicleIds) {
            Vehicle vehicle = vehicleService.getVehicle(availableVehicle);
            VehicleType vehicleType = vehicle.getType();
            stationVehicleCountMap.putIfAbsent(station.getName(), new HashMap<>());
            stationVehicleCountMap.get(station.getName()).putIfAbsent(vehicleType, new ArrayList<>());
            stationVehicleCountMap.get(station.getName()).get(vehicleType).add(vehicle);
        }
        System.out.println(stationVehicleCountMap.toString());

    }

    // Method to search for vehicles
    public Map<Station, Map<VehicleType, List<String>>> searchVehicle(VehicleType vehicleType, double userLatitude, double userLongitude) {
        Map<Station,  Map<VehicleType, List<String>>> searchResult = new HashMap<>();
        //
        List<Station> matchingStations = new ArrayList<>();
        for (Station station : stationService.getStationMap().values()) {
            List<String> vehicles = station.getAvailableVehicles();
            for (String id : vehicles) {
                if (vehicleService.getVehicle(id).getType().equals(vehicleType)) {
                    matchingStations.add(station);
                }
            }
        }

        matchingStations.sort((a, b) -> {
            double priceA = a.getPrices().get(vehicleType);
            double priceB = b.getPrices().get(vehicleType);
            if (priceA != priceB) {
                return Double.compare(priceA, priceB);
            }
            double distanceA = calculateDistance(userLatitude, userLongitude, a.getLatitude(), a.getLongitude());
            double distanceB = calculateDistance(userLatitude, userLongitude, b.getLatitude(), b.getLongitude());
            return Double.compare(distanceA, distanceB);
        });

        for (Station station : matchingStations) {
            List<String> vehicleList = station.getAvailableVehicles();
            Map<VehicleType, List<String>> vehicleDetails = new HashMap<>();
            vehicleDetails.put(vehicleType, vehicleList);
            searchResult.put(station, vehicleDetails);
        }
        return searchResult;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lon2 - lon1, 2));
    }

    private boolean isCarAvailable(Vehicle car, LocalDateTime startDate, LocalDateTime endDate) {
        for (Reservation reservation : reservations.values()) {
            if (reservation.getCar().equals(car)) {
                if (startDate.isBefore(reservation.getEndDate()) && endDate.isAfter(reservation.getStartDate())) {
                    return false;
                }
            }
        }
        return true;
    }

    private String generateReservationId() {
        return "RES" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public synchronized void cancelReservation(String reservationId) {
        Reservation reservation = reservations.remove(reservationId);
        if (reservation != null) {
            reservation.getCar().setAvailable(true);
            reservation.getStation().getAvailableVehicles().add(reservation.getCar().getVehicleId());
        }
    }

}
