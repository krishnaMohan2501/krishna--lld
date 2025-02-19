package com.mc.lld.dscheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class SchedulerNode {
    private final int nodeId;
    private final BlockingQueue<Task> taskQueue;
    private final ExecutorService workerThread;

    public SchedulerNode(int nodeId) {
        this.nodeId = nodeId;
        this.taskQueue = new LinkedBlockingQueue<>();
        this.workerThread = Executors.newSingleThreadExecutor();
        startProcessing();
    }

    private void startProcessing() {
        workerThread.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Task task = taskQueue.take();
                    System.out.println("Node " + nodeId + " executing Task " + task.getTaskId());
                    task.execute();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }


    public void addTask(Task task) {
        taskQueue.offer(task);
    }

    public int getQueueSize() {
        return taskQueue.size();
    }

    public List<Task> drainTasks() {
        List<Task> remainingTasks = new ArrayList<>();
        taskQueue.drainTo(remainingTasks);
        return remainingTasks;
    }

    public void shutdown() {
        workerThread.shutdownNow();
    }
}
