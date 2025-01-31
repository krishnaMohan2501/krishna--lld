package com.mc.lld.conpool;

import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.HashMap;
import java.util.Map;

public class ObjectPoolManager<T> {
    private final int poolSize;
    private final LinkedBlockingQueue<T> pool;
    private final Supplier<T> objectSupplier;
    private final Map<String, Semaphore> userQuotas; // Map to track user quotas
    private final int maxQuotaPerUser;

    public ObjectPoolManager(int poolSize, int maxQuotaPerUser, Supplier<T> objectSupplier) {
        this.poolSize = poolSize;
        this.maxQuotaPerUser = maxQuotaPerUser;
        this.objectSupplier = objectSupplier;
        this.pool = new LinkedBlockingQueue<>(poolSize);
        this.userQuotas = new ConcurrentHashMap<>();

        // Initialize the pool with objects
        for (int i = 0; i < poolSize; i++) {
            pool.offer(objectSupplier.get());
        }
    }

    public T borrowObject(String userId) throws InterruptedException, TimeoutException {
        System.out.println("Thread [" + Thread.currentThread().getName() + "] is waiting for borrow request");

        // Ensure user quota is initialized
        userQuotas.putIfAbsent(userId, new Semaphore(maxQuotaPerUser));

        // Check user's quota
        Semaphore userQuota = userQuotas.get(userId);
        if (!userQuota.tryAcquire(5, TimeUnit.SECONDS)) {
            throw new TimeoutException("User " + userId + " exceeded their max quota of " + maxQuotaPerUser);
        }

        // Borrow an object from the pool
        T object = pool.poll(5, TimeUnit.SECONDS); // Timeout is 5 seconds
        if (object == null) {
            // If borrowing failed, release the quota
            userQuota.release();
            throw new TimeoutException("Timeout while waiting to borrow an object");
        }

        System.out.println("Thread [" + Thread.currentThread().getName() + "] borrowed object for user " + userId + ". Pool size: " + pool.size());
        return object;
    }

    public void returnObject(String userId, T object) throws InterruptedException {
        // Return the object to the pool
        pool.put(object);

        // Release the quota for the user
        Semaphore userQuota = userQuotas.get(userId);
        if (userQuota != null) {
            userQuota.release();
        }

        System.out.println("Thread [" + Thread.currentThread().getName() + "] returned object for user " + userId + ". Pool size: " + pool.size());
    }

    public int getPoolSize() {
        return pool.size();
    }

    public int getUserQuota(String userId) {
        Semaphore userQuota = userQuotas.get(userId);
        return userQuota == null ? maxQuotaPerUser : userQuota.availablePermits();
    }
}
