package com.progressive.minds.chimera.foundational.logging;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class MessageWithContextTest {

    @Test
    void mergeCombinesMessagesAndContexts() {
        HashMap<String, String> context1 = new HashMap<>();
        context1.put("key1", "value1");
        MessageWithContext message1 = new MessageWithContext("Hello", context1);

        HashMap<String, String> context2 = new HashMap<>();
        context2.put("key2", "value2");
        MessageWithContext message2 = new MessageWithContext(" World", context2);

        MessageWithContext result = message1.merge(message2);

        assertEquals("Hello World", result.message());
        assertEquals(2, result.context().size());
        assertEquals("value1", result.context().get("key1"));
        assertEquals("value2", result.context().get("key2"));
    }

    @Test
    void mergeWithNullThrowsException() {
        HashMap<String, String> context = new HashMap<>();
        MessageWithContext message = new MessageWithContext("Hello", context);

        assertThrows(NullPointerException.class, () -> message.merge(null));
    }

    @Test
    void stripMarginRemovesLeadingAndTrailingSpaces() {
        HashMap<String, String> context = new HashMap<>();
        MessageWithContext message = new MessageWithContext("  Hello World  ", context);

        MessageWithContext result = message.stripMargin();

        assertEquals("Hello World", result.message());
        assertEquals(context, result.context());
    }

    @Test
    void messageReturnsCorrectMessage() {
        HashMap<String, String> context = new HashMap<>();
        MessageWithContext message = new MessageWithContext("Hello", context);

        assertEquals("Hello", message.message());
    }

    @Test
    void contextReturnsCorrectContext() {
        HashMap<String, String> context = new HashMap<>();
        context.put("key", "value");
        MessageWithContext message = new MessageWithContext("Hello", context);

        assertEquals(context, message.context());
    }
}