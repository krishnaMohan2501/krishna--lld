package com.mc.lld.ratelimiter1;

public class RateLimiterFactory {
    public static RateLimiter createRateLimiter(String type, int maxRequests, long windowSizeInMillis) {
        switch (type.toLowerCase()) {
            case "fixed":
                return new FixedWindowRateLimiter(maxRequests, windowSizeInMillis);
            case "sliding":
                return new SlidingWindowRateLimiter(maxRequests, windowSizeInMillis);
            default:
                throw new IllegalArgumentException("Unknown rate limiter type");
        }
    }
}
