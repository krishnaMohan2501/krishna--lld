package com.mc.lld.kvstore;

// Custom exception for type safety violations
class TypeMismatchException extends RuntimeException {
    public TypeMismatchException(String message) {
        super(message);
    }
}
