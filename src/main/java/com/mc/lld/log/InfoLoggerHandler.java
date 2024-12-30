package com.mc.lld.log;

public class InfoLoggerHandler extends LoggerHandler {

    public InfoLoggerHandler(LoggerHandler nextLoggerHandler) {
        super(nextLoggerHandler);
    }

    @Override
    public void handleLoggerRequest(int logLevel, String message) {
        if (logLevel == INFO) {
            System.out.println("INFO: " + message);
        } else {
            super.handleLoggerRequest(logLevel, message);
        }
    }
}