package com.mc.lld.conpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class GenericConnectionPool<T> {
    private final BlockingQueue<T> availableConnections;
    private final AtomicInteger totalConnections;
    private final int maxPoolSize;
    private final int timeoutMillis;
    private final ConnectionFactory<T> connectionFactory;
    private final ConnectionValidator<T> validator;

    public GenericConnectionPool(
            int maxPoolSize,
            int timeoutMillis,
            ConnectionFactory<T> connectionFactory,
            ConnectionValidator<T> validator) {
        this.maxPoolSize = maxPoolSize;
        this.timeoutMillis = timeoutMillis;
        this.connectionFactory = connectionFactory;
        this.validator = validator;
        this.availableConnections = new LinkedBlockingQueue<>();
        this.totalConnections = new AtomicInteger(0);

        // Pre-fill the pool with initial connections if necessary
        for (int i = 0; i < maxPoolSize; i++) {
            try {
                availableConnections.offer(createConnection());
                totalConnections.incrementAndGet();
            } catch (Exception e) {
                // Handle creation failure during pool initialization
            }
        }
    }

    public T getConnection() throws Exception {
        T connection = null;

        try {
            // Try to get existing connection from pool
            connection = availableConnections.poll(timeoutMillis, TimeUnit.MILLISECONDS);

            if (connection == null) {
                // If no connection available, throw an exception or handle the timeout
                throw new Exception("Connection pool timeout: No connections available");
            }

            // Validate the connection
            if (!isConnectionValid(connection)) {
                totalConnections.decrementAndGet();
                connection = createConnection();
                totalConnections.incrementAndGet();
            }

            return connection;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new Exception("Interrupted while waiting for connection", e);
        }
    }

    private T createConnection() throws Exception {
        return connectionFactory.createConnection();
    }

    private boolean isConnectionValid(T connection) {
        return validator.isValid(connection);
    }

    public void releaseConnection(T connection) {
        if (isConnectionValid(connection)) {
            availableConnections.offer(connection);
        } else {
            totalConnections.decrementAndGet();
        }
    }

    public void shutdown(ConnectionCloser<T> closer) {
        List<T> remainingConnections = new ArrayList<>();
        availableConnections.drainTo(remainingConnections);

        for (T connection : remainingConnections) {
            try {
                closer.closeConnection(connection);
            } catch (Exception e) {
                // Log error or handle appropriately
            }
        }
    }

    // Functional Interfaces (same as before)
    public interface ConnectionFactory<T> {
        T createConnection() throws Exception;
    }

    public interface ConnectionValidator<T> {
        boolean isValid(T connection);
    }

    public interface ConnectionCloser<T> {
        void closeConnection(T connection) throws Exception;
    }
}


import java.util.concurrent.LinkedBlockingQueue;

public class ObjectPoolManager<T> {
    private static final ObjectPoolManager<?> pool_manager = new ObjectPoolManager<>(5);
    private final int POOL_SIZE;
    private final LinkedBlockingQueue<T> pool;

    private ObjectPoolManager(int poolSize) {
        this.POOL_SIZE = poolSize;
        pool = new LinkedBlockingQueue<>(POOL_SIZE);  // Bounded queue
        for (int i = 0; i < POOL_SIZE; i++) {
            pool.offer(createObject(i)); // Assuming a method to create an object
        }
    }

    private T createObject(int id) {
        // Create the object (adjust based on your object creation logic)
        return (T) new EmailClient(id);
    }

    @SuppressWarnings("unchecked")
    public static <T> ObjectPoolManager<T> getInstance() {
        return (ObjectPoolManager<T>) pool_manager;
    }

    // Borrowing object (blocks if the pool is empty)
    public T borrowObject() throws InterruptedException {
        System.out.println("Thread [" + Thread.currentThread().getName() + "] is waiting for borrow request");
        return pool.take();  // This will block if the pool is empty
    }

    // Returning object (blocks if the pool is full)
    public void returnObject(T object) throws InterruptedException {
        System.out.println("Thread [" + Thread.currentThread().getName() + "] is returning object");
        pool.put(object);  // This will block if the pool is full
    }
}


