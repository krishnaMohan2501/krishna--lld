package com.mc.lld.kvstore;

import java.util.*;

public class KeyValueStore {

    private final Map<String, Object> store = new HashMap<>();
    private final Map<String, ValueMetadata> metadata = new HashMap<>();

    // Helper method to check if value is of supported primitive type
    private boolean isValidPrimitiveType(Object value) {
        return value instanceof String ||
                value instanceof Integer ||
                value instanceof Long ||
                value instanceof Double ||
                value instanceof Float ||
                value instanceof Boolean;
    }

    // Store primitive value
    public void put(String key, Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        // Check if the value is of supported primitive type
        if (!isValidPrimitiveType(value)) {
            throw new IllegalArgumentException("Unsupported value type");
        }

        // Check type safety for existing keys
        if (metadata.containsKey(key)) {
            ValueMetadata meta = metadata.get(key);
            if (meta.isCollection() || !meta.getType().equals(value.getClass())) {
                throw new TypeMismatchException("Type mismatch for key: " + key);
            }
        } else {
            // First insert: store metadata
            metadata.put(key, new ValueMetadata(value.getClass(), false));
        }

        store.put(key, value);
    }

    // Get primitive value
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        if (!store.containsKey(key)) {
            return null;
        }

        ValueMetadata meta = metadata.get(key);
        if (meta.isCollection()) {
            throw new TypeMismatchException("Key contains a collection, use getCollection instead");
        }

        return (T) store.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> Collection<T> getCollection(String key) {
        if (!store.containsKey(key)) {
            return null;
        }

        ValueMetadata meta = metadata.get(key);
        if (!meta.isCollection()) {
            throw new TypeMismatchException("Key contains a primitive value, use get instead");
        }

        return (Collection<T>) store.get(key);
    }

    // Delete key
    public void delete(String key) {
        store.remove(key);
        metadata.remove(key);
    }

    // Delete specific values from collection
    public <T> void deleteFromCollection(String key, Collection<T> values) {
        if (!metadata.containsKey(key) || !metadata.get(key).isCollection()) {
            throw new IllegalArgumentException("Key does not exist or is not a collection");
        }

        Collection<T> existing = getCollection(key);
        existing.removeAll(values);

        // If collection becomes empty, remove the key entirely
        if (existing.isEmpty()) {
            delete(key);
        }
    }

    // Store collection value
    public <T> void putCollection(String key, Collection<T> values, Class<T> elementType) {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("Values cannot be null or empty");
        }

        // Verify all elements are of the same primitive type
        for (T value : values) {
            if (!isValidPrimitiveType(value)) {
                throw new IllegalArgumentException("Collection contains unsupported type");
            }
        }

        // Check type safety for existing keys
        if (metadata.containsKey(key)) {
            ValueMetadata meta = metadata.get(key);
            if (!meta.isCollection() || !meta.getType().equals(elementType)) {
                throw new TypeMismatchException("Type mismatch for key: " + key);
            }
        } else {
            // First insert: store metadata
            metadata.put(key, new ValueMetadata(elementType, true));
        }

        // Store as List or Set based on input type
        if (values instanceof Set) {
            store.put(key, new HashSet<>(values));
        } else {
            store.put(key, new ArrayList<>(values));
        }
    }
}
