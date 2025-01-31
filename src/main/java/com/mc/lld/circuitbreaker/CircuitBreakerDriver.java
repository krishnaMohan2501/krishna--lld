package com.mc.lld.circuitbreaker;

public class CircuitBreakerDriver {
    public static void main(String[] args) {
        CircuitBreaker circuitBreaker = new CircuitBreaker(3, 5000, 1);

        try {
            simulateRequests(circuitBreaker);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void simulateRequests(CircuitBreaker circuitBreaker) throws InterruptedException {
        System.out.println("Initial Circuit State: " + circuitBreaker.getState());

        // Simulate consecutive failures to trigger OPEN state
        for (int i = 1; i <= 3; i++) {
            if (circuitBreaker.allowRequest()) {
                System.out.println("Request " + i + ": Failed");
                circuitBreaker.recordFailure();
            } else {
                System.out.println("Request " + i + ": Rejected");
            }
        }

        System.out.println("Circuit State after failures: " + circuitBreaker.getState());

        // Wait for reset timeout
        System.out.println("Waiting for reset timeout...");
        Thread.sleep(5000);

        // Simulate test requests in HALF_OPEN state
        System.out.println("Circuit State after reset timeout: " + circuitBreaker.getState());

        if (circuitBreaker.allowRequest()) {
            System.out.println("Test Request 1: Succeeded");
            circuitBreaker.recordSuccess(); // Simulating success
        }

        System.out.println("Circuit State after first test request: " + circuitBreaker.getState());

        if (circuitBreaker.allowRequest()) {
            System.out.println("Test Request 2: Failed");
            circuitBreaker.recordFailure(); // Simulating failure
        }

        System.out.println("Circuit State after second test request: " + circuitBreaker.getState());
    }

}
