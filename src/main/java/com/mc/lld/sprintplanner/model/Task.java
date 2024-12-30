package com.mc.lld.sprintplanner.model;

import com.mc.lld.sprintplanner.enums.Status;
import com.mc.lld.sprintplanner.enums.TaskType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class Task {
    private String taskId;
    private TaskType taskType;
    private Status status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String createdBy;
    private String assignedTo;

    public Task(TaskType taskType, String createdBy, String assignedTo) {
        this.taskType = taskType;
        this.status = Status.TODO;
        this.taskId = UUID.randomUUID().toString();
        this.createdBy = createdBy;
        this.assignedTo = assignedTo;
    }
}
