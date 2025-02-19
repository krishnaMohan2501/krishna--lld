package com.mc.lld.multithreading;

import java.util.concurrent.*;

public class ConcurrentLRUCache<K, V> {
    private final int capacity;
    private final ConcurrentHashMap<K, V> cache;
    private final ConcurrentLinkedDeque<K> lruOrder;

    public ConcurrentLRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new ConcurrentHashMap<>(capacity);
        this.lruOrder = new ConcurrentLinkedDeque<>();
    }

    public V get(K key) {
        if (!cache.containsKey(key)) return null;

        // Move key to the end (Most Recently Used position)
        synchronized (this) {
            lruOrder.remove(key);
            lruOrder.addLast(key);
        }
        return cache.get(key);
    }

    public void put(K key, V value) {
        synchronized (this) {
            if (cache.containsKey(key)) {
                lruOrder.remove(key);
            } else if (cache.size() >= capacity) {
                // Remove Least Recently Used (LRU) key
                K oldestKey = lruOrder.pollFirst();
                if (oldestKey != null) cache.remove(oldestKey);
            }
            cache.put(key, value);
            lruOrder.addLast(key);
        }
    }
}

//
//    Best Approach for Interviews
//        If asked for the simplest thread-safe LRU, use LinkedHashMap + ReentrantLock.
//        If asked for a highly concurrent version, go with ConcurrentHashMap + ConcurrentLinkedDeque.
