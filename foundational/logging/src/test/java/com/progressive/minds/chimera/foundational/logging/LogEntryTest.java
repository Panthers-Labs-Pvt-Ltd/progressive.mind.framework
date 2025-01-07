package com.progressive.minds.chimera.foundational.logging;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class LogEntryTest {

    @Test
    void getMessageReturnsCorrectMessage() {
        MessageWithContext messageWithContext = new MessageWithContext("Test message", new HashMap<>());
        LogEntry logEntry = LogEntry.from(messageWithContext);

        assertEquals("Test message", logEntry.getMessage());
    }

    @Test
    void getContextReturnsCorrectContext() {
        HashMap<String, String> context = new HashMap<>();
        context.put("key", "value");
        MessageWithContext messageWithContext = new MessageWithContext("Test message", context);
        LogEntry logEntry = LogEntry.from(messageWithContext);

        assertEquals(context, logEntry.getContext());
    }

    @Test
    void fromCreatesLogEntryWithGivenMessageWithContext() {
        MessageWithContext messageWithContext = new MessageWithContext("Test message", new HashMap<>());
        LogEntry logEntry = LogEntry.from(messageWithContext);

        assertNotNull(logEntry);
        assertEquals("Test message", logEntry.getMessage());
    }

    @Test
    void fromThrowsExceptionWhenMessageWithContextIsNull() {
        assertThrows(NullPointerException.class, () -> LogEntry.from(null));
    }
}