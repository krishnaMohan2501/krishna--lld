package com.mc.lld.elevator;

import lombok.Getter;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Getter
public class Elevator implements Runnable {
    private final int id;
    private final int capacity;
    private final int maxFloor;
    private int currentFloor;
    private Direction direction;
    private final Set<Integer> destinationFloors;
    private final Lock lock;
    private final PriorityQueue<Request> requests;
    private int currentPassengers;
    private boolean isRunning;

    public Elevator(int id, int capacity, int maxFloor) {
        this.id = id;
        this.capacity = capacity;
        this.maxFloor = maxFloor;
        this.currentFloor = 1;
        this.direction = Direction.IDLE;
        this.destinationFloors = new TreeSet<>();
        this.lock = new ReentrantLock();

        this.requests = new PriorityQueue<>((r1, r2) -> {
            if (direction == Direction.UP) {
                return r1.getToFloor() - r2.getToFloor();
            } else {
                return r2.getToFloor() - r1.getToFloor();
            }
        });
        this.currentPassengers = 0;
        this.isRunning = true;
    }

    public boolean addRequest(Request request) {
        lock.lock();
        try {
            if (currentPassengers + 1 <= capacity) {
                requests.add(request);
                destinationFloors.add(request.getToFloor());
                if (direction == Direction.IDLE) {
                    direction = request.getDirection();
                }
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public void processNextFloor() {
        lock.lock();
        try {
            // Check if we need to let passengers off
            if (destinationFloors.contains(currentFloor)) {
                System.out.println("Elevator " + id + " stopping at floor " + currentFloor);
                currentPassengers--;
                destinationFloors.remove(currentFloor);
            }

            // Process requests for current floor
            Iterator<Request> iterator = requests.iterator();
            while (iterator.hasNext()) {
                Request request = iterator.next();
                if (request.getFromFloor() == currentFloor) {
                    if (currentPassengers < capacity) {
                        currentPassengers++;
                        destinationFloors.add(request.getToFloor());
                        iterator.remove();
                    }
                }
            }

            // Update direction and move to next floor
            if (destinationFloors.isEmpty()) {
                direction = Direction.IDLE;
            } else {
                if (direction == Direction.UP && currentFloor < maxFloor) {
                    currentFloor++;
                } else if (direction == Direction.DOWN && currentFloor > 1) {
                    currentFloor--;
                } else {
                    direction = (direction == Direction.UP) ? Direction.DOWN : Direction.UP;
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public void shutdown() {
        isRunning = false;
    }

    @Override
    public void run() {
        processNextFloor();
    }
}
