package com.mc.lld.multithreading;

import java.util.concurrent.*;

public class TaskScheduler {

    // ExecutorService to manage threads with a fixed thread pool
    private final ExecutorService executorService;

    // BlockingQueue to hold tasks when the thread pool is at capacity
    private final BlockingQueue<Runnable> taskQueue;

    // Maximum number of tasks that can be pending in the queue
    private final int queueCapacity;

    // Constructor that accepts the max threads and queue capacity
    public TaskScheduler(int maxThreads, int queueCapacity) {
        this.executorService = Executors.newFixedThreadPool(maxThreads);
        this.taskQueue = new LinkedBlockingQueue<>(queueCapacity);
        this.queueCapacity = queueCapacity;
    }

    // Method to schedule a task. This method should return immediately.
    public boolean scheduleTask(Runnable task) {
        if (taskQueue.remainingCapacity() > 0) {
            // If there's space in the queue, offer the task to the queue.
            boolean addedToQueue = taskQueue.offer(task);
            if (addedToQueue) {
                // Submit a task to the executor when a thread becomes available
                executorService.submit(this::executeTask);
            }
            return addedToQueue;
        } else {
            // If the queue is full, reject the task immediately
            System.out.println("Task queue is full. Task could not be scheduled.");
            return false;
        }
    }

    // Executes tasks from the queue
    private void executeTask() {
        try {
            // Take a task from the queue and run it
            Runnable task = taskQueue.take();
            task.run();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Method to shut down the executor gracefully
    public void shutdown() {
        executorService.shutdown();
    }

    // Main method to test the task scheduler
    public static void main(String[] args) {
        TaskScheduler scheduler = new TaskScheduler(3, 10); // 3 threads, 10 tasks in the queue

        // Simulate client tasks
        for (int i = 0; i < 20; i++) {
            int taskId = i;
            boolean scheduled = scheduler.scheduleTask(() -> {
                try {
                    System.out.println("Executing Task " + taskId);
                    Thread.sleep(1000); // Simulate task execution time
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            if (scheduled) {
                System.out.println("Task " + taskId + " scheduled.");
            } else {
                System.out.println("Task " + taskId + " could not be scheduled.");
            }
        }

        // Shut down the scheduler
        scheduler.shutdown();
    }
}

