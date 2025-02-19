package com.mc.lld.multithreading;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LeakyBucketRateLimiter {

    private final int capacity; // Maximum number of requests the bucket can hold
    private final double rate; // Requests allowed per second
    private final Queue<Long> queue; // Queue to store request timestamps
    private long lastDrip; // Timestamp of the last drip

    public LeakyBucketRateLimiter(int capacity, double rate) {
        this.capacity = capacity;
        this.rate = rate;
        this.queue = new ConcurrentLinkedQueue<>();
        this.lastDrip = System.currentTimeMillis();
    }

    /**
     * Check if a request is allowed.
     *
     * @param key The key to identify the requester (not used in this implementation).
     * @return true if the request is allowed, false otherwise.
     */
    public boolean allow(String key) {
        long currentTime = System.currentTimeMillis();
        drip(currentTime);

        // If the bucket is full, reject the request
        if (queue.size() >= capacity) {
            return false;
        }

        // Add the current request timestamp to the queue
        queue.offer(currentTime);
        return true;
    }

    /**
     * Simulate the leaky bucket drip process.
     *
     * @param currentTime The current timestamp.
     */
    private void drip(long currentTime) {
        // Calculate the time elapsed since the last drip
        long elapsedTime = currentTime - lastDrip;

        // Calculate the number of requests to drip (remove) based on the rate
        long requestsToDrip = (long) (elapsedTime * rate / 1000.0);

        // Drip (remove) the oldest requests from the queue
        while (requestsToDrip > 0 && !queue.isEmpty()) {
            queue.poll();
            requestsToDrip--;
        }

        // Update the last drip time
        lastDrip = currentTime;
    }

    public static void main(String[] args) throws InterruptedException {
        // Example usage
        LeakyBucketRateLimiter rateLimiter = new LeakyBucketRateLimiter(10, 2.0); // 2 requests per second, capacity of 10

        // Simulate requests
        for (int i = 0; i < 20; i++) {
            System.out.println("Request " + (i + 1) + " allowed: " + rateLimiter.allow("user1"));
            Thread.sleep(200); // Simulate request delay
        }
    }
}
