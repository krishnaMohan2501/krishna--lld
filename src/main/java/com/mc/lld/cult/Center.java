package com.mc.lld.cult;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Getter
@Setter
public class Center {

    private String centerId;
    private String name;
    private String location;
    // Map of timeSlot -> Activity
    private final ConcurrentMap<TimeSlot, Activity> slotActivityMap;
    private final ConcurrentMap<String, Activity> activities;

    private LocalTime openingTime;
    private LocalTime closingTime;

    public Center(String name, String location) {
        this.centerId = UUID.randomUUID().toString();
        this.name = name;
        this.location = location;
        this.activities = new ConcurrentHashMap<>();
        this.slotActivityMap = new ConcurrentHashMap<>();
        this.openingTime = LocalTime.of(6, 0); // 6 AM
        this.closingTime = LocalTime.of(22, 0); // 10 PM
    }

    @Getter
    @AllArgsConstructor
    public static class TimeSlot {
        private final LocalTime startTime;
        private final LocalTime endTime;

        public boolean overlaps(TimeSlot other) {
            return !endTime.isBefore(other.startTime) &&
                    !startTime.isAfter(other.endTime);
        }

        public boolean isWithinOperatingHours(LocalTime opening, LocalTime closing) {
            return !startTime.isBefore(opening) && !endTime.isAfter(closing);
        }
    }

    public void addActivity(Activity activity) {
        activities.put(activity.getActivityId(), activity);
    }

    public Activity getActivity(String activityId) {
        return activities.get(activityId);
    }

    public Collection<Activity> getActivities() {
        return activities.values();
    }

    public boolean addActivityToSlot(Activity activity, TimeSlot slot) {
            if (!slot.isWithinOperatingHours(openingTime, closingTime)) {
                return false;
            }

            // Check for slot conflicts
            if (slotActivityMap.keySet().stream()
                    .anyMatch(existingSlot -> existingSlot.overlaps(slot))) {
                return false;
            }

            slotActivityMap.put(slot, activity);
            activities.put(activity.getActivityId(), activity);
            return true;

    }

    public Activity getActivityInSlot(TimeSlot slot) {

        return slotActivityMap.get(slot);

    }

    public Collection<Activity> getActivitiesInTimeRange(LocalTime start, LocalTime end) {
            return slotActivityMap.entrySet().stream()
                    .filter(entry -> !entry.getKey().endTime.isBefore(start) &&
                            !entry.getKey().startTime.isAfter(end))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
    }


}
