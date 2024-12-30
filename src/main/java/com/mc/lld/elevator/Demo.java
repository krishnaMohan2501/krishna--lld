package com.mc.lld.elevator;

public class Demo {

    public static void main(String[] args) {
        // Create an elevator system with 3 elevators, each having a capacity of 4 and maximum floor of 10
        ElevatorSystem elevatorSystem = new ElevatorSystem(3, 4, 10);

        // Test Case 1: Single request
        System.out.println("Test Case 1: Single request from floor 3 to floor 7");
        elevatorSystem.requestElevator(3, 7);

        // Test Case 2: Multiple requests in the same direction
        System.out.println("\nTest Case 2: Multiple requests in the same direction");
        elevatorSystem.requestElevator(2, 8);
        elevatorSystem.requestElevator(4, 6);

        // Test Case 3: Requests in opposite directions
        System.out.println("\nTest Case 3: Requests in opposite directions");
        elevatorSystem.requestElevator(5, 1);
        elevatorSystem.requestElevator(9, 2);

        // Test Case 5: Invalid floor request
        System.out.println("\nTest Case 5: Invalid floor request");
        try {
            elevatorSystem.requestElevator(-1, 5);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught exception: " + e.getMessage());
        }

        try {
            elevatorSystem.requestElevator(2, 11);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught exception: " + e.getMessage());
        }

        // Test Case 6: Simulate elevator system
        System.out.println("\nTest Case 6: Simulate elevator operations");
        elevatorSystem.requestElevator(1, 5);
        elevatorSystem.requestElevator(3, 6);
        elevatorSystem.requestElevator(6, 2);

        // Allow time for system to process requests
        try {
            Thread.sleep(15000); // Let elevators process for 15 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Shut down the elevator system
        elevatorSystem.shutdown();

        System.out.println("\nTest completed");
    }
}
