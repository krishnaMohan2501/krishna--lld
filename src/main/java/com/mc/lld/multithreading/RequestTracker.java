package com.mc.lld.multithreading;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class RequestTracker {

    // Data structure to store request counts grouped by attribute (e.g., IP address or BrowserAgent)
    private final Map<String, Map<String, Integer>> requestCounts = new ConcurrentHashMap<>();

    // Queue to track requests and their timestamps for eviction
    private final Queue<RequestEntry> requestQueue = new LinkedBlockingQueue<>();

    // Lock for thread-safe operations
    private final ReentrantLock lock = new ReentrantLock();

    // Time window in milliseconds (e.g., 1 minute)
    private final long timeWindow;

    public RequestTracker(long timeWindow) {
        this.timeWindow = timeWindow;
    }

    // Record a new request
    public void recordRequest(String attribute, String value) {
        lock.lock();
        try {
            // Remove outdated entries
            evictOutdatedEntries();

            // Update the request count for the given attribute and value
            requestCounts.computeIfAbsent(attribute, k -> new ConcurrentHashMap<>())
                    .merge(value, 1, Integer::sum);

            // Add the request to the queue with the current timestamp
            requestQueue.offer(new RequestEntry(attribute, value, System.currentTimeMillis()));
        } finally {
            lock.unlock();
        }
    }

    // Get the request count for a specific attribute and value
    public int getRequestCount(String attribute, String value) {
        lock.lock();
        try {
            // Remove outdated entries
            evictOutdatedEntries();

            // Return the count for the given attribute and value
            return requestCounts.getOrDefault(attribute, Collections.emptyMap())
                    .getOrDefault(value, 0);
        } finally {
            lock.unlock();
        }
    }

    // Evict outdated entries from the queue and request counts
    private void evictOutdatedEntries() {
        long currentTime = System.currentTimeMillis();
        while (!requestQueue.isEmpty()) {
            RequestEntry entry = requestQueue.peek();
            if (currentTime - entry.timestamp > timeWindow) {
                // Remove the entry from the queue
                requestQueue.poll();

                // Decrement the count for the attribute and value
                Map<String, Integer> attributeCounts = requestCounts.get(entry.attribute);
                if (attributeCounts != null) {
                    attributeCounts.computeIfPresent(entry.value, (k, v) -> v - 1);
                    if (attributeCounts.get(entry.value) <= 0) {
                        attributeCounts.remove(entry.value);
                    }
                }
            } else {
                break; // Stop evicting if the next entry is within the time window
            }
        }
    }

    // Inner class to represent a request entry
    private static class RequestEntry {
        String attribute;
        String value;
        long timestamp;

        RequestEntry(String attribute, String value, long timestamp) {
            this.attribute = attribute;
            this.value = value;
            this.timestamp = timestamp;
        }
    }

    // Example usage
    public static void main(String[] args) throws InterruptedException {
        RequestTracker tracker = new RequestTracker(60000); // 1-minute time window

        // Simulate incoming requests
        Runnable requestSimulator = () -> {
            Random random = new Random();
            String[] ips = {"192.168.1.1", "192.168.1.2", "192.168.1.3"};
            String[] browsers = {"Chrome", "Firefox", "Safari"};

            for (int i = 0; i < 100; i++) {
                String ip = ips[random.nextInt(ips.length)];
                String browser = browsers[random.nextInt(browsers.length)];

                tracker.recordRequest("IP", ip);
                tracker.recordRequest("BrowserAgent", browser);

                try {
                    Thread.sleep(random.nextInt(100)); // Simulate request delay
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        // Start multiple threads to simulate concurrent requests
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(requestSimulator);
            threads[i].start();
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            thread.join();
        }

        // Print the request counts
        System.out.println("IP Request Counts: " + tracker.requestCounts.get("IP"));
        System.out.println("BrowserAgent Request Counts: " + tracker.requestCounts.get("BrowserAgent"));
    }
}
