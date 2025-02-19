package com.mc.lld.multithreading;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadSafeLRUCache<K, V> {
    private final int capacity;
    private final Map<K, V> cache; // Thread-safe HashMap
    private final LinkedHashMap<K, V> lruOrder; // To maintain access order
    private final ReentrantLock lock = new ReentrantLock(); // Ensures thread safety

    public ThreadSafeLRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new ConcurrentHashMap<>(capacity);
        this.lruOrder = new LinkedHashMap<>(capacity, 0.75f, true); // Access-order LRU
    }

    public V get(K key) {
        lock.lock();
        try {
            if (!cache.containsKey(key)) {
                return null;
            }
            // Move accessed key to the most recently used position
            V value = cache.get(key);
            lruOrder.remove(key);
            lruOrder.put(key, value);
            return value;
        } finally {
            lock.unlock();
        }
    }

    public void put(K key, V value) {
        lock.lock();
        try {
            if (cache.containsKey(key)) {
                // Update existing key's value and move it to MRU position
                cache.put(key, value);
                lruOrder.remove(key);
                lruOrder.put(key, value);
            } else {
                if (cache.size() >= capacity) {
                    // Remove the least recently used element
                    Map.Entry<K, V> oldestEntry = lruOrder.entrySet().iterator().next();
                    K oldestKey = oldestEntry.getKey();
                    cache.remove(oldestKey);
                    lruOrder.remove(oldestKey);
                }
                // Add new key-value pair
                cache.put(key, value);
                lruOrder.put(key, value);
            }
        } finally {
            lock.unlock();
        }
    }
}



//import java.util.LinkedHashMap;
//        import java.util.Map;
//        import java.util.concurrent.locks.ReentrantLock;
//
//public class SimpleThreadSafeLRU<K, V> {
//    private final int capacity;
//    private final Map<K, V> cache;
//    private final ReentrantLock lock = new ReentrantLock();
//
//    public SimpleThreadSafeLRU(int capacity) {
//        this.capacity = capacity;
//        this.cache = new LinkedHashMap<>(capacity, 0.75f, true) {
//            @Override
//            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
//                return size() > capacity; // Remove the LRU entry
//            }
//        };
//    }
//
//    public V get(K key) {
//        lock.lock();
//        try {
//            return cache.getOrDefault(key, null);
//        } finally {
//            lock.unlock();
//        }
//    }
//
//    public void put(K key, V value) {
//        lock.lock();
//        try {
//            cache.put(key, value);
//        } finally {
//            lock.unlock();
//        }
//    }
//}
//
