package com.mc.lld.dscheduler;

public class Task {

    private final int taskId;
    private final Runnable action;

    public Task(int taskId, Runnable action) {
        this.taskId = taskId;
        this.action = action;
    }

    public void execute() {
        action.run();
    }

    public int getTaskId() {
        return taskId;
    }
}
