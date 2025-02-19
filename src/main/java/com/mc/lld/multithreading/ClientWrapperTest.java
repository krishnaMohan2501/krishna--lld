package com.mc.lld.multithreading;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

class Client {
    public void init() {
        // Simulate connection setup
        System.out.println(Thread.currentThread().getName() + " initializing connection...");
        try { Thread.sleep(1000); } catch (InterruptedException e) { }
        System.out.println(Thread.currentThread().getName() + " initialization done.");
    }

    public void request() {
        // Simulate request handling
        System.out.println(Thread.currentThread().getName() + " making a request...");
        try { Thread.sleep(500); } catch (InterruptedException e) { }
    }

    public void close() {
        // Simulate closing connection
        System.out.println(Thread.currentThread().getName() + " closing connection...");
        try { Thread.sleep(1000); } catch (InterruptedException e) { }
        System.out.println(Thread.currentThread().getName() + " connection closed.");
    }
}

class ClientWrapper extends Client {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition initCompleted = lock.newCondition();
    private final Condition requestCompleted = lock.newCondition();

    private boolean isInitialized = false;
    private boolean isClosed = false;
    private int activeRequests = 0;

    @Override
    public void init() {
        lock.lock();
        try {
            if (isInitialized || isClosed) {
                return; // Already initialized or closed, ignore
            }

            super.init();  // Call parent class init()
            isInitialized = true;
            initCompleted.signalAll(); // Notify waiting threads that init is done
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void request() {
        lock.lock();
        try {
            // Wait until initialization is done
            while (!isInitialized) {
                try {
                    initCompleted.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore the interrupt status
                    throw new RuntimeException("Thread interrupted while waiting for initialization", e);
                }
            }

            if (isClosed) {
                throw new IllegalStateException("Connection is already closed!");
            }

            activeRequests++;
        } finally {
            lock.unlock();
        }

        try {
            super.request();  // Call parent class request()
        } finally {
            lock.lock();
            try {
                activeRequests--;
                if (activeRequests == 0) {
                    requestCompleted.signalAll(); // Notify that requests are finished
                }
            } finally {
                lock.unlock();
            }
        }
    }


    @Override
    public void close() {
        lock.lock();
        try {
            if (isClosed) {
                return; // Already closed, ignore
            }

            while (activeRequests > 0) {
                requestCompleted.await(); // Wait until all requests are finished
            }

            super.close(); // Call parent class close()
            isClosed = true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }
}

public class ClientWrapperTest {
    public static void main(String[] args) {
        ClientWrapper client = new ClientWrapper();

        Thread t1 = new Thread(client::init, "Thread-1");
        Thread t2 = new Thread(client::request, "Thread-2");
        Thread t3 = new Thread(client::close, "Thread-3");
        Thread t4 = new Thread(client::request, "Thread-4");

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}


//InterruptedException, which is a checked exception.
