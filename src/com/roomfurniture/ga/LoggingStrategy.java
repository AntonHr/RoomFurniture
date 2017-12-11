package com.roomfurniture.ga;

public interface LoggingStrategy {
    public enum LoggingLevel {
        DEBUG,
        INFO,
        WARNING,
        ERROR
    };
    void log(LoggingLevel level, String message);

    default void info(String message) {
        log(LoggingLevel.INFO, message);
    }

    default void debug(String message) {
        log(LoggingLevel.DEBUG, message);
    }

    default void warning(String message) {
        log(LoggingLevel.WARNING, message);
    }

    default void error(String message) {
        log(LoggingLevel.ERROR, message);
    }
}
