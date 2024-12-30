package com.mc.lld.log;

public class DebugLoggerHandler extends LoggerHandler {

    public DebugLoggerHandler(LoggerHandler nextLoggerHandler) {
        super(nextLoggerHandler);
    }

    @Override
    public void handleLoggerRequest(int logLevel, String message) {
        if (logLevel == DEBUG) {
            System.out.println("DEBUG: " + message);
        } else {
            super.handleLoggerRequest(logLevel, message);
        }
    }
}
