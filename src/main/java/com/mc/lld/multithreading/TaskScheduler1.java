package com.mc.lld.multithreading;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

class TaskScheduler1 {
    private final Map<Integer, List<Integer>> dependencyGraph = new HashMap<>();
    private final Map<Integer, Runnable> taskMap = new HashMap<>();
    private final Map<Integer, AtomicInteger> inDegree = new HashMap<>();
    private final CustomThreadPool1 threadPool;

    public TaskScheduler1(int numThreads) {
        threadPool = new CustomThreadPool1(numThreads);
    }

    public void addTask(int taskId, Runnable task, List<Integer> dependencies) {
        taskMap.put(taskId, task);
        dependencyGraph.putIfAbsent(taskId, new ArrayList<>());
        inDegree.putIfAbsent(taskId, new AtomicInteger(0));

        for (int dep : dependencies) {
            dependencyGraph.computeIfAbsent(dep, k -> new ArrayList<>()).add(taskId);
            inDegree.putIfAbsent(dep, new AtomicInteger(0));
            inDegree.get(taskId).incrementAndGet();
        }
    }

    public void execute() {
        Queue<Integer> readyQueue = new LinkedList<>();

        // Find tasks with no dependencies
        for (Map.Entry<Integer, AtomicInteger> entry : inDegree.entrySet()) {
            if (entry.getValue().get() == 0) {
                readyQueue.add(entry.getKey());
            }
        }

        while (!readyQueue.isEmpty()) {
            List<Thread> activeThreads = new ArrayList<>();

            int size = readyQueue.size();
            for (int i = 0; i < size; i++) {
                int taskId = readyQueue.poll();
                Runnable task = taskMap.get(taskId);

                Thread thread = new Thread(() -> {
                    task.run();
                    // After finishing, reduce dependencies of next tasks
                    synchronized (this) {
                        for (int dependentTask : dependencyGraph.getOrDefault(taskId, Collections.emptyList())) {
                            if (inDegree.get(dependentTask).decrementAndGet() == 0) {
                                readyQueue.add(dependentTask);
                            }
                        }
                        notify();
                    }
                });

                threadPool.submit(thread);
                activeThreads.add(thread);
            }

            // Wait for current batch to complete
            for (Thread thread : activeThreads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        threadPool.shutdown();
    }

    public static void main(String[] args) {
        TaskScheduler1 scheduler = new TaskScheduler1(3);

        scheduler.addTask(1, () -> {
            System.out.println("Task 1 executing");
        }, Arrays.asList());

        scheduler.addTask(2, () -> {
            System.out.println("Task 2 executing");
        }, Arrays.asList(1));

        scheduler.addTask(3, () -> {
            System.out.println("Task 3 executing");
        }, Arrays.asList(1));

        scheduler.addTask(4, () -> {
            System.out.println("Task 4 executing");
        }, Arrays.asList(2, 3));

        scheduler.addTask(5, () -> {
            System.out.println("Task 5 executing");
        }, Arrays.asList(3));

        scheduler.addTask(6, () -> {
            System.out.println("Task 6 executing");
        }, Arrays.asList(4, 5));

        scheduler.execute();
    }

    class CustomThreadPool1 {
        private final List<Worker> workers;
        private final Queue<Runnable> taskQueue;
        private volatile boolean isShutdown = false;

        public CustomThreadPool1(int numThreads) {
            taskQueue = new LinkedList<>();
            workers = new ArrayList<>();
            for (int i = 0; i < numThreads; i++) {
                Worker worker = new Worker();
                workers.add(worker);
                worker.start();
            }
        }

        public synchronized void submit(Runnable task) {
            if (isShutdown) throw new IllegalStateException("ThreadPool is shutting down");
            taskQueue.offer(task);
            notify(); // Notify waiting worker threads
        }

        public synchronized void shutdown() {
            isShutdown = true;
            for (Worker worker : workers) worker.interrupt();
        }

        private class Worker extends Thread {
            public void run() {
                while (true) {
                    Runnable task;
                    synchronized (CustomThreadPool1.this) {
                        while (taskQueue.isEmpty() && !isShutdown) {
                            try {
                                CustomThreadPool1.this.wait();
                            } catch (InterruptedException e) {
                                return; // Exit on shutdown
                            }
                        }
                        if (isShutdown) return;
                        task = taskQueue.poll();
                    }
                    task.run();
                }
            }
        }
    }
}

