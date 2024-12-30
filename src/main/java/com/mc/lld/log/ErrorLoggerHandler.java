package com.mc.lld.log;

public class ErrorLoggerHandler extends LoggerHandler {

    public ErrorLoggerHandler(LoggerHandler nextLoggerHandler) {
        super(nextLoggerHandler);
    }

    @Override
    public void handleLoggerRequest(int logLevel, String message) {
        if (logLevel == ERROR) {
            System.out.println("ERROR: " + message);
        } else {
            super.handleLoggerRequest(logLevel, message);
        }
    }
}
