package com.mc.lld.elevator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ElevatorSystem {
    private final List<Elevator> elevators;
    private final int maxFloor;
    private final ExecutorService executorService;

    public ElevatorSystem(int numElevators, int capacity, int maxFloor) {
        this.elevators = new ArrayList<>();
        this.maxFloor = maxFloor;
        this.executorService = Executors.newFixedThreadPool(numElevators);
        for (int i = 0; i < numElevators; i++) {
            Elevator elevator = new Elevator(i + 1, capacity, maxFloor);
            elevators.add(elevator);
            executorService.submit(() -> {
                while (elevator.isRunning()) {
                    elevator.run();
                    try {
                        Thread.sleep(1000); // Simulate delay for processing floors
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
    }

    public void requestElevator(int fromFloor, int toFloor) {
        if (fromFloor < 1 || fromFloor > maxFloor || toFloor < 1 || toFloor > maxFloor) {
            throw new IllegalArgumentException("Invalid floor number");
        }

        Request request = new Request(fromFloor, toFloor);
        Elevator bestElevator = findBestElevator(request);
        System.out.println("Found best elevatorId = " + bestElevator.getId() + "from floor= " + fromFloor + " to " + toFloor);
        bestElevator.addRequest(request);
    }

    private Elevator findBestElevator(Request request) {
        Elevator bestElevator = null;
        int minDistance = Integer.MAX_VALUE;

        for (Elevator elevator : elevators) {
            int distance = Math.abs(elevator.getCurrentFloor() - request.getFromFloor());
            Direction elevatorDirection = elevator.getDirection();

            // Prioritize elevators going in the same direction
            if (elevatorDirection == request.getDirection() || distance < minDistance) {
                    minDistance = distance;
                    bestElevator = elevator;

            }
        }
        return bestElevator;
    }

    public void shutdown() {
        for (Elevator elevator : elevators) {
            elevator.shutdown();
        }
        executorService.shutdown();
    }

}
