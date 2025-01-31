package com.mc.lld.rlimiter;

public class RateLimiterDriver {
    public static void main(String[] args) throws InterruptedException {
        RateLimiter rateLimiter = new RateLimiter(5); // Allow 5 requests per second

        String userId = "user123";

        // Simulate 10 requests from the same user
        for (int i = 0; i < 10; i++) {
            if (rateLimiter.tryAcquire(userId)) {
                System.out.println("Request " + (i + 1) + " allowed");
            } else {
                System.out.println("Request " + (i + 1) + " denied");
            }
            Thread.sleep(200); // Simulate a delay of 200ms between requests
        }

        rateLimiter.shutdown(); // Clean up resources
    }
}
