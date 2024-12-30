package com.mc.lld.log;

public class LoggerHandler {

    public static final int INFO = 1;
    public static final int DEBUG = 2;
    public static final int ERROR = 3;

    LoggerHandler nextLoggerHandler;

    public LoggerHandler(LoggerHandler nextLoggerHandler) {
        this.nextLoggerHandler = nextLoggerHandler;
    }

    public void handleLoggerRequest(int logLevel, String message) {
        if (nextLoggerHandler != null) {
            nextLoggerHandler.handleLoggerRequest(logLevel, message);
        }
    }
}
