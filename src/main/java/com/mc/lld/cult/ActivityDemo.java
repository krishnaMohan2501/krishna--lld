package com.mc.lld.cult;

import java.time.LocalDateTime;

public class ActivityDemo {

    public static void run() {
        BookingService bookingService = BookingService.getInstance();

        // Setup test data
        Center center = new Center("FitLife Center", "Downtown");
        bookingService.addCenter(center);

        LocalDateTime now = LocalDateTime.now();
        Activity yoga = new Activity("Yoga", 20,
                now.plusDays(1).withHour(10).withMinute(0),
                now.plusDays(1).withHour(11).withMinute(0),
                center, true, 2);

        center.addActivity(yoga);

        User user1 = new User("U1", "John Doe", "john@example.com");
        User user2 = new User("U2", "Jane Smith", "jane@example.com");
        bookingService.addUser(user1);
        bookingService.addUser(user2);

        bookingService.bookActivity(user1.getUserId(), yoga.getActivityId());

        // Print results
        System.out.println("Final participants count: " + yoga.getParticipants().size());
    }
}
