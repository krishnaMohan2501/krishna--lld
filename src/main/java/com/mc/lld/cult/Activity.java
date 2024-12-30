package com.mc.lld.cult;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Getter
@Setter
public class Activity {

    private String activityId;
    private String name;
    private int capacity;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Center center;
    private Set<User> participants;
    Queue<User> waitingList;
    private boolean isCancellable;
    private int cancellationWindowHours;
    private final ReadWriteLock lock;

    public Activity(String name, int capacity, LocalDateTime startTime,
                    LocalDateTime endTime, Center center, boolean isCancellable, int cancellationWindowHours) {
        this.activityId = UUID.randomUUID().toString();
        this.name = name;
        this.capacity = capacity;
        this.startTime = startTime;
        this.endTime = endTime;
        this.center = center;
        this.participants = ConcurrentHashMap.newKeySet();
        this.waitingList = new ConcurrentLinkedQueue<>();
        this.lock = new ReentrantReadWriteLock();
        this.isCancellable = isCancellable;
        this.cancellationWindowHours = cancellationWindowHours;
    }

    public boolean isAvailable() {
        lock.readLock().lock();
        try {
            return participants.size() < capacity;
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean addParticipant(User user) {
        lock.writeLock().lock();
        try {
            if (participants.size() < capacity) {
                return participants.add(user);
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean removeParticipant(User user) {
        lock.writeLock().lock();
        try {
            boolean removed = participants.remove(user);
            if (removed) {
                promoteFromWaitingList();
            }
            return removed;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void promoteFromWaitingList() {
        User nextUser = waitingList.poll();
        if (nextUser != null) {
            participants.add(nextUser);
            // In real system, notify user
        }
    }

    public boolean addToWaitingList(User user) {
        return waitingList.offer(user);
    }

    public boolean hasTimeConflict(LocalDateTime start, LocalDateTime end) {
        return !(end.isBefore(startTime) || start.isAfter(endTime));
    }
}
