package com.mc.lld.log;

public class App {

    public static void main(String[] args) {
        LoggerHandler logger = new InfoLoggerHandler(new DebugLoggerHandler(new ErrorLoggerHandler(null)));

        logger.handleLoggerRequest(LoggerHandler.INFO, "hello to info logger");
        logger.handleLoggerRequest(LoggerHandler.DEBUG, "hello to debug logger");
        logger.handleLoggerRequest(LoggerHandler.ERROR, "hello to error logger");
    }


}
