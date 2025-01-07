package com.progressive.minds.chimera.foundational.logging;

import com.progressive.minds.chimera.foundational.logging.LogKey.LogKey;
import com.progressive.minds.chimera.foundational.logging.LogKey.LogKeys;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class MDCTest {

    @Test
    void ofCreatesMDCWithGivenKeyAndValue() {
        LogKey key = LogKeys.EXECUTOR_ID;
        Object value = "testValue";
        MDC mdc = MDC.of(key, value);

        assertEquals(key, mdc.key());
        assertEquals(value, mdc.value());
    }

    @Test
    void ofThrowsExceptionWhenValueIsMessageWithContext() {
        LogKey key = LogKeys.EXECUTOR_ID;
        MessageWithContext value = new MessageWithContext("message", new HashMap<>());

        assertThrows(IllegalArgumentException.class, () -> MDC.of(key, value));
    }

    @Test
    void keyReturnsCorrectKey() {
        LogKey key = LogKeys.EXECUTOR_ID;
        MDC mdc = MDC.of(key, "testValue");

        assertEquals(key, mdc.key());
    }

    @Test
    void valueReturnsCorrectValue() {
        Object value = "testValue";
        MDC mdc = MDC.of(LogKeys.EXECUTOR_ID, value);

        assertEquals(value, mdc.value());
    }
}