package com.mc.lld.cult;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Getter
@Setter
public class User {

    private String userId;
    private String name;
    private String email;
    private final Set<Activity> bookedActivities;
    private final ReadWriteLock lock;

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.bookedActivities = new CopyOnWriteArraySet<>();
        this.lock = new ReentrantReadWriteLock();
    }

    public boolean addBooking(Activity activity) {
        lock.writeLock().lock();
        try {
            return bookedActivities.add(activity);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean removeBooking(Activity activity) {
        lock.writeLock().lock();
        try {
            return bookedActivities.remove(activity);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Set<Activity> getBookedActivities() {
        lock.readLock().lock();
        try {
            return new HashSet<>(bookedActivities);
        } finally {
            lock.readLock().unlock();
        }
    }

}
