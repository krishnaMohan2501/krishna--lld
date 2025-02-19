package com.mc.lld.multithreading;

import java.util.*;

public class DS {
    public static void main(String[] args) {
        DistributedScheduler scheduler = new DistributedScheduler(3);

        // Add tasks to specific nodes
        scheduler.addTask("Node-1", new Task("Task1"));
        scheduler.addTask("Node-2", new Task("Task2"));
        scheduler.addTask("Node-3", new Task("Task3"));
        scheduler.addTask("Node-1", new Task("Task4"));
        scheduler.addTask("Node-2", new Task("Task5"));

        // Simulate some time for tasks to execute
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Rebalance the load
        scheduler.rebalanceLoad();

        // Shutdown the scheduler
        scheduler.shutdown();
    }
}


class Task implements Runnable {
    private final String taskId;

    public Task(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public void run() {
        System.out.println("Executing task: " + taskId + " on thread: " + Thread.currentThread().getName());
        // Simulate task execution time
        try {
            Thread.sleep(1000); // Simulate task execution time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Node {
    private final String nodeId;
    private final Queue<Task> taskQueue = new LinkedList<>();
    private final Thread workerThread;
    private volatile boolean isRunning = true;

    public Node(String nodeId) {
        this.nodeId = nodeId;
        this.workerThread = new Thread(this::processTasks);
        this.workerThread.start();
    }

    public void addTask(Task task) {
        synchronized (taskQueue) {
            taskQueue.add(task);
            taskQueue.notify(); // Notify the worker thread that a new task is available
        }
    }

    public Task pollTask() {
        synchronized (taskQueue) {
            return taskQueue.poll(); // Safely remove and return a task from the queue
        }
    }

    private void processTasks() {
        while (isRunning) {
            Task task;
            synchronized (taskQueue) {
                while (taskQueue.isEmpty() && isRunning) {
                    try {
                        taskQueue.wait(); // Wait for a task to be added
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                if (!isRunning) {
                    return;
                }
                task = taskQueue.poll();
            }
            if (task != null) {
                task.run();
            }
        }
    }

    public int getLoad() {
        synchronized (taskQueue) {
            return taskQueue.size();
        }
    }

    public void shutdown() {
        isRunning = false;
        synchronized (taskQueue) {
            taskQueue.notify(); // Wake up the worker thread to exit
        }
        try {
            workerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class DistributedScheduler {
    private final List<Node> nodes;
    private final Map<String, Node> nodeMap;

    public DistributedScheduler(int numNodes) {
        this.nodes = new ArrayList<>();
        this.nodeMap = new HashMap<>();
        for (int i = 0; i < numNodes; i++) {
            String nodeId = "Node-" + (i + 1);
            Node node = new Node(nodeId);
            nodes.add(node);
            nodeMap.put(nodeId, node);
        }
    }

    public void addTask(String nodeId, Task task) {
        Node node = nodeMap.get(nodeId);
        if (node != null) {
            node.addTask(task);
        } else {
            throw new IllegalArgumentException("Node with ID " + nodeId + " does not exist.");
        }
    }

    public void rebalanceLoad() {
        int totalLoad = 0;
        for (Node node : nodes) {
            totalLoad += node.getLoad();
        }
        int averageLoad = totalLoad / nodes.size();

        for (Node node : nodes) {
            while (node.getLoad() > averageLoad) {
                Task task = getTaskFromNode(node);
                if (task != null) {
                    Node leastLoadedNode = getLeastLoadedNode();
                    leastLoadedNode.addTask(task);
                }
            }
        }
    }

    private Task getTaskFromNode(Node node) {
        // This method should be implemented to safely remove a task from the node's queue
        // For simplicity, we assume that the node's queue is thread-safe and we can poll a task
        synchronized (node) {
            return node.getLoad() > 0 ? node.pollTask() : null;
        }
    }

    private Node getLeastLoadedNode() {
        Node leastLoadedNode = nodes.get(0);
        for (Node node : nodes) {
            if (node.getLoad() < leastLoadedNode.getLoad()) {
                leastLoadedNode = node;
            }
        }
        return leastLoadedNode;
    }

    public void shutdown() {
        for (Node node : nodes) {
            node.shutdown();
        }
    }
}
