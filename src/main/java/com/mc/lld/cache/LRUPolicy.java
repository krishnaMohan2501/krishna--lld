package com.mc.lld.cache;

import java.util.HashSet;
import java.util.LinkedList;

public class LRUPolicy<K> implements EvictPolicy<K> {
    private LinkedList<K> dataQueue;
    private HashSet<K> keySet;  // Just for O(1) presence check
    // OR we could just check dataQueue.contains(key) but that would be O(n)

    public LRUPolicy() {
        this.dataQueue = new LinkedList<>();
        this.keySet = new HashSet<>();
    }

    @Override
    public void keyAccessed(K key) {
        if (keySet.contains(key)) {
            dataQueue.remove(key);
        } else {
            keySet.add(key);  // Add to set only for new keys
        }
        dataQueue.addLast(key);  // Add to end of queue in both cases
    }

    @Override
    public K evict() {
        if (dataQueue.isEmpty()) return null;
        K key = dataQueue.removeFirst();
        keySet.remove(key);
        return key;
    }
}