package com.mc.lld.ratelimiter1;

public interface RateLimiter {
    boolean allowRequest(String clientId);
}
