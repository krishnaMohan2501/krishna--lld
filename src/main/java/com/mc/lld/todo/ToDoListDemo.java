package com.mc.lld.todo;

import com.mc.lld.todo.observer.ActivityLog;
import com.mc.lld.todo.service.ToDoListService;
import com.mc.lld.todo.task.Task;
import com.mc.lld.todo.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ToDoListDemo {
    public static void main(String[] args) {

        ToDoListService todoList = ToDoListService.getInstance();

        // observer
        ActivityLog activityLog = new ActivityLog();
        todoList.addObserver(activityLog);

        System.out.println("======= User creation starting ===================.");
        // Create users
        User user1 = new User("1", "Pankaj", "pankaj@example.com");
        User user2 = new User("2", "Sumit", "sumit@example.com");

        System.out.println("======= User creation completed ===================.");

        // Create tasks
        System.out.println("\n");
        System.out.println("============ Task creation started ==================.");

        Task task1 = new Task("1", "Buy groceries from mart", user1, Arrays.asList("shopping"), null);
        Task task2 = new Task("2", "Meeting", user2, Arrays.asList("work"), LocalDateTime.now().plusSeconds(1));
        Task task3 = new Task("3", "Examination", user1,  Arrays.asList("Exam"), null);

        System.out.println("============ Task creation completed ==================.");

        System.out.println("\n");
        System.out.println("Adding tasks in todo list");
        todoList.addTask(task1);
        todoList.addTask(task2);
        todoList.addTask(task3);
        System.out.println("Successfully added tasks in todo list");

        // Modify a task
        System.out.println("\n");
        System.out.println("modifying task....");
        Task updateTask = new Task();
        updateTask.setDescription("Cat exam");
        todoList.modifyTask(task3.getId(), updateTask);

        // Get task by id
        System.out.println("\n");
        System.out.println("fetching task by id...");
        Task task = todoList.getTask(task1.getId());
        if (task == null) {
            System.out.println("Unable to fetch task for taskId = " + task1.getId());
        } else {
            System.out.println("Fetched task: " + task + " for task id: "  + task1.getId());
        }

        // Mark a task as completed
        System.out.println("\n");
        System.out.println("marking task as completed....");
        todoList.markTaskAsCompleted(task1.getId());

        // printing stat
        System.out.println("\n");
        System.out.println("fetching stats....");
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(5);
        String stat = todoList.getStats(startDateTime, endDateTime);
        if (stat !=null) {
            System.out.println("Unable to fetch stats.");
        } else {
            System.out.println("Successfully fetched Stat:: " + stat);

        }

        // printing activity log
        System.out.println("\n");
        System.out.println("fetching activity logs...");
        List<String> activityLogList = todoList.getActivityLog(startDateTime, endDateTime);
        if (activityLogList.size() > 1) {
            for (String log : activityLogList) {
                System.out.println(log);
            }
        } else {
            System.out.println("Unable to fetch activity logs.");
        }

        System.out.println("\n");
        System.out.println("checking for future task...");
        todoList.checkFutureTasks();

        System.out.println("\n");
        System.out.println("removing task from todo list...");
        todoList.removeTask(task3.getId());

    }
}
