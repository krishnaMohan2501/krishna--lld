package com.mc.lld.sprintplanner.model;

import com.mc.lld.sprintplanner.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class Sprint {

    private String name;
    private String id;
    private Map<String, Task> tasks;
    private Status status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String userId;

    public Sprint(String name, LocalDateTime startTime, LocalDateTime endTime, String userId) {
        this.name = name;
        this.id = UUID.randomUUID().toString();
        this.tasks = new ConcurrentHashMap<>();
        this.status = Status.TODO;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userId = userId;
    }
}
