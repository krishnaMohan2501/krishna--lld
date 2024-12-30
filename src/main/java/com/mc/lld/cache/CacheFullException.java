package com.mc.lld.cache;

public class CacheFullException extends RuntimeException {
    public CacheFullException(String message) {
        super(message);
    }
}
