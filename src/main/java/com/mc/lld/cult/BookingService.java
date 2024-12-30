package com.mc.lld.cult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class BookingService {

    private final ConcurrentMap<String, Center> centers;
    private final ConcurrentMap<String, User> users;

    private BookingService() {
        this.centers = new ConcurrentHashMap<>();
        this.users = new ConcurrentHashMap<>();
    }

    private static volatile BookingService INSTANCE;

    public static BookingService getInstance() {
        if (INSTANCE == null) {
            synchronized (BookingService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BookingService();
                }
            }
        }
        return INSTANCE;
    }

    public void addCenter(Center center) {
        centers.put(center.getCenterId(), center);
    }

    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public boolean bookActivity(String userId, String activityId) {
        User user = users.get(userId);
        Activity activity = findActivity(activityId);

        if (user == null || activity == null) {
            throw new IllegalArgumentException("Invalid user or activity");
        }

        // Check for time conflicts
        Set<Activity> userActivities = user.getBookedActivities();
        boolean hasConflict = userActivities.stream()
                .anyMatch(booked -> activity.hasTimeConflict(booked.getStartTime(), booked.getEndTime()));

        if (hasConflict) {
            return false;
        }

        if (activity.addParticipant(user)) {
            user.addBooking(activity);
            return true;
        } else {
            activity.addToWaitingList(user);
            return false;
        }
    }

    public Activity findActivity(String activityId) {
        return centers.values().stream()
                .map(center -> center.getActivity(activityId))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public List<Activity> searchAvailableActivities(String centerId,
                                                     LocalDateTime startTime, LocalDateTime endTime) {

        Center center = centers.get(centerId);
        if (center == null) {
            throw new IllegalArgumentException("Invalid center");
        }

        return center.getActivities()
                .parallelStream()
                .filter(activity -> activity.isAvailable() &&
                        !activity.hasTimeConflict(startTime, endTime))
                .collect(Collectors.toCollection(CopyOnWriteArrayList::new));
    }

    public boolean cancelBooking(String userId, String activityId) {
        User user = users.get(userId);
        Activity activity = findActivity(activityId);

        if (user == null || activity == null) {
            throw new IllegalArgumentException("Invalid user or activity");
        }

        if (!activity.isCancellable()) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.plusHours(activity.getCancellationWindowHours()).isAfter(activity.getStartTime())) {
            return false;
        }

        if (activity.removeParticipant(user)) {
            user.removeBooking(activity);
            return true;
        }
        return false;
    }

}
