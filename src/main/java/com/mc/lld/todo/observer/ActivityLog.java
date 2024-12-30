package com.mc.lld.todo.observer;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ActivityLog implements Observer {
    private List<String> log = new ArrayList<>();

    @Override
    public void update(String message) {
        log.add(LocalDateTime.now() + ": " + message);
    }

    public List<String> getLog() {
        return log;
    }

    public List<String> getLog(LocalDateTime startTime, LocalDateTime endTime) {
        List<String> filteredLog = new ArrayList<>();
        for (String logEntry : log) {
            String[] logParts = logEntry.split(": ", 2);
            LocalDateTime logTime = LocalDateTime.parse(logParts[0]);
            if (startTime.isBefore(logTime) && endTime.isAfter(logTime)) {
                filteredLog.add(logEntry);
            }
        }
        return filteredLog;
    }
}
