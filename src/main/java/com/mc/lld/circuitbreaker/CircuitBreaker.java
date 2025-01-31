package com.mc.lld.circuitbreaker;

import java.util.concurrent.atomic.AtomicInteger;

public class CircuitBreaker {
    private volatile State state; // Use volatile to ensure visibility across threads
    private AtomicInteger failureCount; // total request count
    private AtomicInteger testRequestCount; // test result count to check if still issue is there
    private final int failureThreshold;
    private final long resetTimeout;
    private final int maxTestRequests;
    private volatile long lastFailureTime; // Volatile for thread-safe visibility

    public enum State {
        CLOSED,
        OPEN,
        HALF_OPEN
    }

    public CircuitBreaker(int failureThreshold, long resetTimeout, int maxTestRequests) {
        this.state = State.CLOSED;
        this.failureCount = new AtomicInteger(0);
        this.testRequestCount = new AtomicInteger(0);
        this.failureThreshold = failureThreshold;
        this.resetTimeout = resetTimeout;
        this.maxTestRequests = maxTestRequests;
    }

    public boolean allowRequest() {
        updateState();

        switch (state) {
            case CLOSED:
                return true;
            case OPEN:
                return false;
            case HALF_OPEN:
                // Allow only up to maxTestRequests in HALF_OPEN state
                if (testRequestCount.incrementAndGet() <= maxTestRequests) {
                    return true;
                } else {
                    testRequestCount.decrementAndGet(); // Rollback if exceeding maxTestRequests
                    return false;
                }
            default:
                return false;
        }
    }

    public void recordSuccess() {
        if (state == State.HALF_OPEN) {
            state = State.CLOSED;
            failureCount.set(0);
            testRequestCount.set(0);
        } else if (state == State.CLOSED) {
            failureCount.set(0);
        }
    }

    public void recordFailure() {
        failureCount.incrementAndGet();
        lastFailureTime = System.currentTimeMillis();

        if (state == State.HALF_OPEN || failureCount.get() >= failureThreshold) {
            state = State.OPEN;
            testRequestCount.set(0);
        }
    }

    private void updateState() {
        if (state == State.OPEN) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastFailureTime >= resetTimeout) {
                state = State.HALF_OPEN;
                testRequestCount.set(0);
            }
        }
    }

    public State getState() {
        return state;
    }
}

