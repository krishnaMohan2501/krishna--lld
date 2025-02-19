package com.mc.lld.multithreading;

import java.util.LinkedList;
import java.util.List;

public class CustomThreadPool {

    // List to hold tasks
    private final List<Runnable> taskQueue = new LinkedList<>();
    private final List<Thread> workers = new LinkedList<>();
    private final int poolSize;
    private boolean isShuttingDown = false;

    public CustomThreadPool(int poolSize) {
        this.poolSize = poolSize;
        initializeWorkers();
    }

    // Initialize worker threads
    private void initializeWorkers() {
        for (int i = 0; i < poolSize; i++) {
            Thread worker = new Thread(new Worker());
            workers.add(worker);
            worker.start();
        }
    }

    // Submit task to the pool
    public synchronized void submit(Runnable task) {
        if (isShuttingDown) {
            throw new IllegalStateException("ThreadPool is shutting down, no new tasks can be submitted.");
        }
        taskQueue.add(task);
        // Notify a worker thread that there is a task to execute
        notify();
    }

    // Gracefully shut down the thread pool
    public synchronized void shutdown() {
        isShuttingDown = true;
        for (Thread worker : workers) {
            worker.interrupt(); // Interrupt workers to allow them to finish
        }
    }

    // Worker class that will execute tasks
    private class Worker implements Runnable {
        @Override
        public void run() {
            while (true) {
                Runnable task = null;
                synchronized (CustomThreadPool.this) {
                    // If the pool is shutting down and the queue is empty, break the loop
                    if (isShuttingDown && taskQueue.isEmpty()) {
                        break;
                    }
                    // If there are no tasks, wait until notified
                    while (taskQueue.isEmpty() && !isShuttingDown) {
                        try {
                            CustomThreadPool.this.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    if (!taskQueue.isEmpty()) {
                        task = taskQueue.remove(0);
                    }
                }
                if (task != null) {
                    task.run();
                }
            }
        }
    }

    // Main method to test the custom thread pool
    public static void main(String[] args) {
        CustomThreadPool pool = new CustomThreadPool(10);

        // Submitting tasks to the pool
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            pool.submit(() -> {
                System.out.println("Executing Task " + taskId + " by " + Thread.currentThread().getName());
                try {
                    Thread.sleep(500); // Simulate work
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // Shutdown the pool after all tasks are done
        try {
            Thread.sleep(3000); // Wait for tasks to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.shutdown();
    }
}

