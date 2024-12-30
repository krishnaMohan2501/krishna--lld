package com.mc.lld.cache;

public interface Storage<K, V> {
    void add(K key, V value);

    V get(K key);

    void remove(K key);
}
