package com.mc.lld.multithreading;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentHashMap;

public class BadPwdRateLimiter {

    private final int maxFailedAttempts;
    private final long timeWindow;
    private final long blockTime;

    // Map to store failed attempts by user key
    private final ConcurrentHashMap<String, Queue<Long>> failedAttempts;
    // Map to store the block time for each user
    private final ConcurrentHashMap<String, Long> blockStatus;

    public BadPwdRateLimiter(int maxFailedAttempts, long timeWindow, long blockTime) {
        this.maxFailedAttempts = maxFailedAttempts;
        this.timeWindow = timeWindow;
        this.blockTime = blockTime;
        this.failedAttempts = new ConcurrentHashMap<>();
        this.blockStatus = new ConcurrentHashMap<>();
    }

    /**
     * Attempts to login with the given user key.
     *
     * @param userKey The key representing the user (e.g., username or IP).
     * @return true if login is successful (not blocked and within failed attempt limits), false otherwise.
     */
    public boolean attemptLogin(String userKey) {
        long currentTime = System.currentTimeMillis();

        // Check if the user is blocked
        if (isBlocked(userKey, currentTime)) {
            System.out.println("User " + userKey + " is blocked. Please try again later.");
            return false;  // User is blocked
        }

        // Record a failed login attempt
        recordFailedAttempt(userKey, currentTime);

        // Check if the user has exceeded the max failed attempts within the time window
        if (getFailedAttemptCount(userKey, currentTime) > maxFailedAttempts) {
            blockUser(userKey, currentTime);  // Block the user
            System.out.println("User " + userKey + " exceeded max failed attempts. Blocking user.");
            return false;  // Failed due to exceeding max attempts
        }

        // Proceed with the login attempt (e.g., check password, etc.)
        // This part can be modified based on actual login logic.

        return true;  // Successful login (if password is correct)
    }

    /**
     * Records a failed login attempt by the user.
     *
     * @param userKey The key representing the user.
     * @param currentTime The current timestamp.
     */
    private synchronized void recordFailedAttempt(String userKey, long currentTime) {
        failedAttempts.putIfAbsent(userKey, new ConcurrentLinkedQueue<>());
        Queue<Long> attempts = failedAttempts.get(userKey);
        attempts.offer(currentTime);

        // Remove failed attempts older than the time window
        while (!attempts.isEmpty() && currentTime - attempts.peek() > timeWindow) {
            attempts.poll();  // Remove stale attempts
        }
    }

    /**
     * Gets the number of failed login attempts within the time window.
     *
     * @param userKey The key representing the user.
     * @param currentTime The current timestamp.
     * @return The number of failed attempts within the time window.
     */
    private int getFailedAttemptCount(String userKey, long currentTime) {
        Queue<Long> attempts = failedAttempts.getOrDefault(userKey, new ConcurrentLinkedQueue<>());
        // Remove failed attempts older than the time window
        attempts.removeIf(timestamp -> currentTime - timestamp > timeWindow);
        return attempts.size();
    }

    /**
     * Checks if the user is blocked.
     *
     * @param userKey The key representing the user.
     * @param currentTime The current timestamp.
     * @return true if the user is blocked, false otherwise.
     */
    private synchronized boolean isBlocked(String userKey, long currentTime) {
        Long blockTimeStamp = blockStatus.get(userKey);
        if (blockTimeStamp == null) {
            return false;  // User is not blocked
        }
        if (currentTime - blockTimeStamp > blockTime) {
            // Unblock the user if the block time has passed
            blockStatus.remove(userKey);
            return false;  // User is no longer blocked
        }
        return true;  // User is still blocked
    }

    /**
     * Blocks the user for the specified block time duration.
     *
     * @param userKey The key representing the user.
     * @param currentTime The current timestamp.
     */
    private synchronized void blockUser(String userKey, long currentTime) {
        blockStatus.put(userKey, currentTime);  // Set block time for the user
    }

    public static void main(String[] args) throws InterruptedException {
        // Initialize the rate limiter with max 3 failed attempts in 10 seconds, block for 30 seconds
        BadPwdRateLimiter rateLimiter = new BadPwdRateLimiter(3, 10000, 30000);

        // Simulate login attempts
        System.out.println(rateLimiter.attemptLogin("user1"));  // Successful
        System.out.println(rateLimiter.attemptLogin("user1"));  // Failed
        System.out.println(rateLimiter.attemptLogin("user1"));  // Failed
        System.out.println(rateLimiter.attemptLogin("user1"));  // Blocked (exceeded max attempts)

        // Wait for 30 seconds before trying again
        Thread.sleep(30000);
        System.out.println(rateLimiter.attemptLogin("user1"));  // Successful (blocked time has passed)
    }
}

