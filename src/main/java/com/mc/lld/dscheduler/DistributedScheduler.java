package com.mc.lld.dscheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DistributedScheduler {

    private final ConcurrentHashMap<Integer, SchedulerNode> nodes;
    private final int maxLoadPerNode;

    public DistributedScheduler(int numNodes, int maxLoadPerNode) {
        this.nodes = new ConcurrentHashMap<>();
        this.maxLoadPerNode = maxLoadPerNode;
        for (int i = 1; i <= numNodes; i++) {
            nodes.put(i, new SchedulerNode(i));
        }
    }

    public void addTask(int nodeId, Task task) {
        if (nodes.containsKey(nodeId)) {
            nodes.get(nodeId).addTask(task);
        } else {
            throw new IllegalArgumentException("Invalid Node ID");
        }
    }

    public void rebalanceLoad() {
        List<Task> overloadedTasks = new ArrayList<>();
        for (SchedulerNode node : nodes.values()) {
            if (node.getQueueSize() > maxLoadPerNode) {
                overloadedTasks.addAll(node.drainTasks());
            }
        }

        if (!overloadedTasks.isEmpty()) {
            System.out.println("Rebalancing " + overloadedTasks.size() + " tasks across nodes...");
            distributeTasksEvenly(overloadedTasks);
        }
    }

    private void distributeTasksEvenly(List<Task> tasks) {
        List<SchedulerNode> nodeList = new ArrayList<>(nodes.values());
        int index = 0;
        for (Task task : tasks) {
            nodeList.get(index % nodeList.size()).addTask(task);
            index++;
        }
    }

    public void shutdown() {
        for (SchedulerNode node : nodes.values()) {
            node.shutdown();
        }
    }
}
