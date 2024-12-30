package com.mc.lld.kvstore;

// Base class for storing value metadata
class ValueMetadata {
    private final Class<?> type;
    private final boolean isCollection;

    public ValueMetadata(Class<?> type, boolean isCollection) {
        this.type = type;
        this.isCollection = isCollection;
    }

    public Class<?> getType() {
        return type;
    }

    public boolean isCollection() {
        return isCollection;
    }
}
