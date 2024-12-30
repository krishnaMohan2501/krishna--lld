package com.mc.lld.cache;

public interface EvictPolicy<K> {

    void keyAccessed(K key);
    K evict();
}
