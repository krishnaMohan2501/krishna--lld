package com.mc.lld.cache;

import java.util.HashMap;
import java.util.Map;

public class HashMapStorage<K, V> implements Storage<K, V> {

    Map<K, V> mapStore;
    private final int limit;

    public HashMapStorage(int limit) {
        this.limit = limit;
        mapStore = new HashMap<>();
    }

    private boolean isFull() {
        return mapStore.size() == limit;
    }

    @Override
    public void add(K key, V value) {
        if (isFull()) {
            throw new CacheFullException("cache is full");
        }
        mapStore.put(key, value);
    }

    @Override
    public V get(K key) {
        if (!mapStore.containsKey(key))
            throw new NotFoundException(key + " key does not exist");
        return mapStore.get(key);
    }

    @Override
    public void remove(K key) {
        if (!mapStore.containsKey(key))
            throw new RuntimeException(key + " key does not exist");
        mapStore.remove(key);
    }

    @Override
    public String toString() {
        return "HashMapStorage{" +
                "mapStore=" + mapStore +
                '}';
    }
}
