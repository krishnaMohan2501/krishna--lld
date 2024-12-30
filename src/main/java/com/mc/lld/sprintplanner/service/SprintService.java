package com.mc.lld.sprintplanner.service;

import com.mc.lld.sprintplanner.enums.Status;
import com.mc.lld.sprintplanner.enums.TaskType;
import com.mc.lld.sprintplanner.model.Sprint;
import com.mc.lld.sprintplanner.model.Task;
import com.mc.lld.sprintplanner.model.User;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SprintService {

    private static final int MAX_TASKS = 20;

    Map<String, Sprint> sprintMap;
    Map<String, Task> taskMap;
    private Map<String, List<String>> userTasks;

    private SprintService() {
        this.sprintMap = new ConcurrentHashMap<>();
        this.taskMap = new ConcurrentHashMap<>();
        this.userTasks = new ConcurrentHashMap<>();
    }

    private static volatile SprintService INSTANCE;

    public static SprintService getInstance() {
        if (INSTANCE == null) {
            synchronized (SprintService.class) {
                INSTANCE = new SprintService();
            }
        }
        return INSTANCE;
    }


    public Sprint createSprint(String name, LocalDateTime startTime, LocalDateTime endTime, User user) {
        if (startTime.isAfter(endTime)) {
            throw new IllegalStateException("Invalid start and end time. please correct it.");
        }
        Sprint sprint = new Sprint(name, startTime, endTime, user.getId());
        sprintMap.put(sprint.getId(), sprint);

        System.out.println("Sprint id = " + sprint.getId() + "got created successfully.!!!");
        return sprint;
    }

    public Sprint getSprintById(String id) {
        if (sprintMap.containsKey(id)) {
            return sprintMap.get(id);
        }
        return null;
    }

    public void addTaskToSprint(@NonNull Task task, @NonNull Sprint sprint) {
        if (sprint.getTasks().size() >= MAX_TASKS) {
            throw new IllegalStateException("Sprint task limit reached");
        }
        synchronized ((sprint.getTasks())) {
            if (sprint.getTasks().size() >= MAX_TASKS) {
                throw new IllegalStateException("Sprint task limit reached");
            }
            sprint.getTasks().put(task.getTaskId(), task);
        }
    }

    public Task createTask(@NonNull TaskType taskType, User createdBy, User assignedTo) {
        Task task = new Task(taskType, createdBy.getId(), assignedTo.getId());
        System.out.println("Task id = " + task.getTaskId() + "got created successfully.!!!");
        taskMap.put(task.getTaskId(), task);
        synchronized (userTasks) {
            userTasks.computeIfAbsent(assignedTo.getId(), k -> Collections.synchronizedList(new ArrayList<>()));
            userTasks.get(assignedTo.getId()).add(task.getTaskId());
        }
        return task;
    }

    public void startTask(String taskId, LocalDateTime localDateTime) {
        Task task = taskMap.get(taskId);
        task.setStartTime(localDateTime);
    }

    public void endTask(String taskId, LocalDateTime localDateTime) {
        Task task = taskMap.get(taskId);
        task.setEndTime(localDateTime);
    }

    public void removeTask(Task task, Sprint sprint) {
        Task removedTask = taskMap.remove(task.getTaskId());
        if (removedTask == null) {
            throw new IllegalArgumentException("Task does not exist");
        }

        userTasks.values().forEach(taskList -> {
            synchronized (taskList) {
                taskList.remove(task.getTaskId());
                sprint.getTasks().remove(task.getTaskId());
            }
        });
    }

    public void changeTaskStatus(String taskId, Status newStatus) {
        Task task = taskMap.get(taskId);
        if (task == null) {
            throw new IllegalArgumentException("Task does not exist");
        }

        synchronized (task) {
            Status currentStatus = task.getStatus();

            if ((Status.TODO.equals(currentStatus) && Status.IN_PROGESS.equals(newStatus)) ||
                    (Status.IN_PROGESS.equals(currentStatus) && (Status.TODO.equals(newStatus) ||
                            Status.DONE.equals(newStatus)))) {

                if (Status.IN_PROGESS.equals(newStatus)) {
                    String userId = task.getAssignedTo();
                    long inProgressCount = userTasks.getOrDefault(userId, Collections.emptyList()).stream()
                            .filter(tid -> Status.IN_PROGESS.equals(taskMap.get(tid).getStatus()))
                            .count();

                    if (inProgressCount >= 2) {
                        System.out.println("User have more than 2 tasks in IN-PROGRESS");
                        throw new IllegalStateException("User cannot have more than 2 tasks in IN-PROGRESS");
                    }
                }
                task.setStatus(newStatus);
                System.out.println("STATUS got changed successfully.");
            } else {
                throw new IllegalArgumentException("Invalid status change");
            }
        }
    }


    public List<String> getTaskUser(String userId, String sprintId) {
        List<String> taskIds = new ArrayList<>();
        Sprint sprint = sprintMap.get(sprintId);
        Map<String, Task> tasks = sprint.getTasks();
        // filter the task by userId
        for (Map.Entry<String, Task> entry : tasks.entrySet()) {
            if (userId.equals(entry.getValue().getAssignedTo())) {
                taskIds.add(entry.getValue().getTaskId());
            }
        }
        return taskIds;
    }

    public List<String> getDelayedTask(String sprintId) {
        List<String> delayedTasks = new ArrayList<>();
        Sprint sprint = sprintMap.get(sprintId);
        Map<String, Task> tasks = sprint.getTasks();
        for (Map.Entry<String, Task> entry : tasks.entrySet()) {
            if (sprint.getEndTime().isBefore(LocalDateTime.now()) || entry.getValue().getEndTime() != null
                    && entry.getValue().getEndTime().isAfter(sprint.getEndTime())) {
                delayedTasks.add(entry.getValue().getTaskId());
            }
        }
        return delayedTasks;
    }
}
