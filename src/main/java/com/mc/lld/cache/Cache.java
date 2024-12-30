package com.mc.lld.cache;

/*
    Cache has two components
    - Eviction policy
    - Storage
 */
public class Cache<K, V> {
    private EvictPolicy<K> evictPolicy;
    private Storage<K, V> storage;

    public Cache(EvictPolicy<K> evictPolicy, Storage<K, V> storage) {
        this.evictPolicy = evictPolicy;
        this.storage = storage;
    }

    public V get(K key) {
        try {
            V value = this.storage.get(key);
            this.evictPolicy.keyAccessed(key);
            return value;
        } catch (NotFoundException e) {
            System.out.println("key not found");
            return null;
        }
    }

    public void put(K key, V value) {
        try {
            this.storage.add(key, value);
            this.evictPolicy.keyAccessed(key);
            System.out.println(storage);

        } catch (CacheFullException e) {
            System.out.println("cache is full, retying ...");
            K keyToEvict = this.evictPolicy.evict();
            this.storage.remove(keyToEvict);
            System.out.println(keyToEvict + " key is removed");
            put(key, value);
        }
    }
}
