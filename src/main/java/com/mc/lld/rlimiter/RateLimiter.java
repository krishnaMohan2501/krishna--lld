package com.mc.lld.rlimiter;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimiter {
    private final ConcurrentHashMap<String, TokenBucket> buckets; // Maps userId to TokenBucket
    private final int maxRequestsPerSecond; // Max allowed requests per second
    private final ScheduledExecutorService scheduler; // For periodic cleanup of expired buckets

    public RateLimiter(int maxRequestsPerSecond) {
        this.buckets = new ConcurrentHashMap<>();
        this.maxRequestsPerSecond = maxRequestsPerSecond;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();

        // Schedule cleanup of expired buckets every minute
        this.scheduler.scheduleAtFixedRate(
                this::cleanupExpiredBuckets,
                1, // Initial delay
                1, // Period
                TimeUnit.MINUTES // Time unit
        );
    }

    // Inner class representing the token bucket for each user
    private static class TokenBucket {
        private final AtomicInteger tokens; // Tracks available tokens
        private final int maxTokens; // Max capacity of tokens in the bucket
        private long lastRefillTimestamp; // Last refill time in milliseconds
        private final long refillIntervalMs; // Time interval for token refill (1000ms = 1 second)

        public TokenBucket(int maxTokens) {
            this.tokens = new AtomicInteger(maxTokens); // Start with a full bucket
            this.maxTokens = maxTokens;
            this.lastRefillTimestamp = Instant.now().toEpochMilli(); // Set the initial timestamp
            this.refillIntervalMs = 1000; // 1 second per refill
        }

        // Tries to consume a token; returns true if successful, false otherwise
        public synchronized boolean tryConsume() {
            refill(); // Ensure tokens are refilled before consuming
            if (tokens.get() > 0) { // Check if any tokens are available
                tokens.decrementAndGet(); // Consume one token
                return true; // Allow the request
            }
            return false; // Deny the request
        }

        // Returns the number of remaining tokens
        public synchronized int getRemainingTokens() {
            refill(); // Ensure tokens are refilled before checking
            return tokens.get();
        }

        // Refills tokens based on the elapsed time since the last refill
        private void refill() {
            long now = Instant.now().toEpochMilli();
            long timePassed = now - lastRefillTimestamp;

            if (timePassed >= refillIntervalMs) {
                // Calculate how many periods (seconds) have passed
                int periods = (int) (timePassed / refillIntervalMs);

                // Add one token per period (not maxTokens * periods)
                int tokensToAdd = periods;

                // Ensure we don't exceed maxTokens
                int currentTokens = tokens.get();
                int newTokens = Math.min(currentTokens + tokensToAdd, maxTokens);

                tokens.set(newTokens); // Update token count
                lastRefillTimestamp = now; // Update last refill timestamp
            }
        }

        // Checks if the bucket is expired (inactive for more than 1 hour)
        public boolean isExpired() {
            return Instant.now().toEpochMilli() - lastRefillTimestamp > TimeUnit.HOURS.toMillis(1);
        }
    }

    // Attempts to acquire a token for the given userId
    public boolean tryAcquire(String userId) {
        TokenBucket bucket = buckets.computeIfAbsent(
                userId,
                k -> new TokenBucket(maxRequestsPerSecond) // Create a new bucket if none exists
        );
        return bucket.tryConsume(); // Try to consume a token
    }

    // Gets the remaining tokens for the given userId
    public int getRemainingTokens(String userId) {
        TokenBucket bucket = buckets.get(userId); // Fetch the user's bucket
        return bucket != null ? bucket.getRemainingTokens() : maxRequestsPerSecond; // Return remaining tokens or max capacity
    }

    // Periodically removes expired buckets to save memory
    private void cleanupExpiredBuckets() {
        buckets.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    // Shuts down the scheduler gracefully
    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(1, TimeUnit.MINUTES)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
