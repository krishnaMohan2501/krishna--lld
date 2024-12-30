package com.mc.lld.todo.service;

import com.mc.lld.todo.observer.*;
import com.mc.lld.todo.observer.Observer;
import com.mc.lld.todo.task.Task;
import com.mc.lld.todo.task.TaskStatus;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ToDoListService {

    // <task id, Task>
    private final Map<String, Task> tasks;
    // <task id, task> -> task where activation date  in future.
    private Map<String, Task> futureTasks;
    // list of observers
    private List<Observer> observers;

    private static volatile ToDoListService instance;

    private ToDoListService() {
        tasks = new ConcurrentHashMap<>();
        futureTasks = new ConcurrentHashMap<>();
        this.observers = new ArrayList<>();
    }

    public static synchronized ToDoListService getInstance() {
        if (instance == null) {
            synchronized (ToDoListService.class) {
                instance = new ToDoListService();
            }
        }
        return instance;
    }

    // add the observer in subject
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    // notify all.
    private void notifyObservers(String message) {
        for (Observer observer : this.observers) {
            observer.update(message);
        }
    }

    /**
     * Function to add task in todo list.
     * @param task Task
     */
    public void addTask(@NonNull Task task) {
        if (task.getActivationDate().isAfter(LocalDateTime.now())) {
            futureTasks.put(task.getId(), task);
            notifyObservers("Scheduled to add in future: " + task.getId());
            System.out.println("Scheduled the task for future. task id=" + task.getId());
        } else {
            tasks.put(task.getId(), task);
            notifyObservers("Added: " + task.getId());
            System.out.println("Successfully added the task. task id =" + task.getId());
        }
    }

    /**
     * function to modify or update the Task based on id
     * @param taskId String
     * @param updatedTask Task
     */
    public void modifyTask(@NonNull String taskId, @NonNull Task updatedTask) {
        Task existingTask = tasks.get(taskId);
        if (existingTask != null) {
            synchronized (existingTask) {
                if (!updatedTask.getDescription().isEmpty()) {
                    existingTask.setDescription(updatedTask.getDescription());
                }
                if (Objects.nonNull(updatedTask.getDueDate())) {
                    existingTask.setDueDate(updatedTask.getDueDate());
                }
                if (updatedTask.getTags().size() > 0) {
                    existingTask.setTags(updatedTask.getTags());
                }
                notifyObservers("Modified: " + existingTask.getId());
                System.out.println("Successfully modified task. task id = " + taskId);
            }
        } else {
            System.out.println("Unable to modify task. task id = " + taskId);
        }
    }

    /**
     * Function is used to remove task from todo list.
     * @param taskId String
     */
    public void removeTask(@NonNull String taskId) {
        Task task = tasks.remove(taskId);
        if (task != null) {
            notifyObservers("Removed: " + task.getId());
            System.out.println("Successfully removed task id = " + taskId);
        } else {
            System.out.println("Unable to  removed task. task id = " + taskId);
        }
    }

    /**
     * Function to fetch Task by taskId.
     * @param taskId String
     * @return Task
     */
    public Task getTask(@NonNull String taskId) {
        for (Task task : tasks.values()) {
            if (task.getId().equalsIgnoreCase(taskId)) {
               return task;
            }
        }
        return null;
    }

    public void markTaskAsCompleted(@NonNull String taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            synchronized (task) {
                task.setStatus(TaskStatus.COMPLETED);
                notifyObservers("Completed: " + taskId);
                tasks.remove(taskId);
                System.out.println("Successfully marked task id: " + taskId + " as completed.");
            }
        } else {
            System.out.println("Unable to to mark invalid taskId= " + taskId);
        }
    }

    public String getStats(@NonNull LocalDateTime startTime, @NonNull LocalDateTime endTime) {
        if (startTime.isAfter(endTime)) {
            System.out.println("Invalid date range provided.");
            return "";
        }
        int added = 0, completed = 0, spilled = 0;
        for (Observer observer : observers) {
            if (observer instanceof ActivityLog) {
                List<String> logs = ((ActivityLog) observer).getLog(startTime, endTime);
                for (String log : logs) {
                    // can be part of task status
                    if (log.contains("Added: ")) {
                        added++;
                    } else if (log.contains("Completed: ")) {
                        completed++;
                    } else if (log.contains("Spilled: ")) {
                        spilled++;
                    }
                }
            }
        }
        return "Stats for period (" + startTime + " to " + endTime + "):\n" +
                "Added: " + added + "\n" +
                "Completed: " + completed + "\n" +
                "Spilled: " + spilled;
    }

    public List<String> getActivityLog(@NonNull LocalDateTime startTime, @NonNull LocalDateTime endTime) {
        List<String> activityLogResults = new ArrayList<>();
        if (startTime.isAfter(endTime)) {
            System.out.println("Invalid date range provided.");
            return activityLogResults;
        }
        for (Observer observer : observers) {
            if (observer instanceof ActivityLog) {
                List<String> logs = ((ActivityLog) observer).getLog(startTime, endTime);
                activityLogResults.addAll(logs);
            }
        }
        return activityLogResults;
    }

    public void checkFutureTasks() {
        LocalDateTime currentTime = LocalDateTime.now();
        Iterator<Map.Entry<String, Task>> iterator = futureTasks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Task> entry = iterator.next();
            if (!currentTime.isBefore(entry.getValue().getActivationDate())) {
                tasks.put(entry.getValue().getId(), entry.getValue());
                notifyObservers("Added: " + entry.getValue());
                iterator.remove();
                System.out.println("Successfully added task to todo list which was scheduled.");
            }
        }
    }
}
