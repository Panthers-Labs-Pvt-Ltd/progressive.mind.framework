package com.progressive.minds.chimera.foundational.logging;


import java.util.HashMap;

/**
 * Represents a log entry being created by MessageWithContext.
 */

public class LogEntry {
    private final MessageWithContext messageWithContext;

    private LogEntry(MessageWithContext messageWithContext) {
        this.messageWithContext = messageWithContext;
    }

    public String getMessage() {
        return messageWithContext.message();
    }

    public HashMap<String, String> getContext() {
        return messageWithContext.context();
    }

    public static LogEntry from(MessageWithContext messageWithContext) {
        if (messageWithContext == null) {
            throw new NullPointerException("MessageWithContext cannot be null");
        }
        return new LogEntry(messageWithContext);
    }
}

