package com.mc.lld.todo.task;

import com.mc.lld.todo.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@Getter
@Setter
@ToString
public class Task {
    private String id;
    private String description;
    private LocalDateTime dueDate; // when is the deadline
    private LocalDateTime addedDate; // got added in system
    private int priority;
    private TaskStatus status;
    private User assignedUser;
    private List<String> tags;
    private LocalDateTime activationDate; // future date

    public Task(String id, String description, User assignedUser, List<String> tags, LocalDateTime activationDate) {
        this.id = id;
        this.description = description;
        this.status = TaskStatus.PENDING;
        this.assignedUser = assignedUser;
        this.tags = tags;
        this.addedDate = LocalDateTime.now();
        this.activationDate = activationDate != null ? activationDate : LocalDateTime.now();
        this.dueDate = this.activationDate.plusDays(1);
    }
//
//    public Task() {
//
//    }
//
//
//    // Getters and setters
//    public String getId() {
//        return id;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public int getPriority() {
//        return priority;
//    }
//
//    public LocalDateTime getDueDate() {
//        return dueDate;
//    }
//
//    public TaskStatus getStatus() {
//        return status;
//    }
//
//    public User getAssignedUser() {
//        return assignedUser;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public void setDueDate(LocalDateTime dueDate) {
//        this.dueDate = dueDate;
//    }
//
//    public void setPriority(int priority) {
//        this.priority = priority;
//    }
//
//    public void setStatus(TaskStatus status) {
//        this.status = status;
//    }
//
//    public LocalDateTime getAddedDate() {
//        return addedDate;
//    }
//
//    public LocalDateTime getActivationDate() {
//        return activationDate;
//    }
//
//    public void setActivationDate(LocalDateTime activationDate) {
//        this.activationDate = activationDate;
//    }

}
