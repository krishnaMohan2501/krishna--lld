package com.mc.lld.dscheduler;

public class DistributedSchedulerMain {

    public static void main(String[] args) throws InterruptedException {
        DistributedScheduler scheduler = new DistributedScheduler(3, 5);

        // Adding tasks to nodes
        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            scheduler.addTask((i % 3) + 1, new Task(taskId, () -> {
                try {
                    Thread.sleep(1000); // Simulate task execution
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }));
        }

        // Wait for execution
        Thread.sleep(5000);

        // Trigger rebalance
        scheduler.rebalanceLoad();

        // Shutdown system
        Thread.sleep(5000);
        scheduler.shutdown();
    }
}
