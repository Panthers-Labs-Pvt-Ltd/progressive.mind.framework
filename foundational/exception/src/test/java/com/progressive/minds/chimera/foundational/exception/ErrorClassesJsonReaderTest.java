package com.progressive.minds.chimera.foundational.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ErrorClassesJsonReaderTest {

    private ErrorClassesJsonReader errorClassesJsonReader;
    private URL mockUrl;

    @BeforeEach
    void setUp() {
        mockUrl = Mockito.mock(URL.class);
        errorClassesJsonReader = new ErrorClassesJsonReader(Collections.singletonList(mockUrl));
    }

    @Test
    void getErrorMessage_withValidErrorClassAndParameters_returnsFormattedMessage() {
        Map<String, String> params = new HashMap<>();
        params.put("param1", "value1");
        String result = errorClassesJsonReader.getErrorMessage("mainErrorClass", params);
        assertEquals("Expected formatted message", result);
    }

    @Test
    void getErrorMessage_withInvalidErrorClass_throwsException() {
        Map<String, String> params = new HashMap<>();
        params.put("param1", "value1");
        assertThrows(Exception.class, () -> errorClassesJsonReader.getErrorMessage("invalidErrorClass", params));
    }

    @Test
    void getMessageTemplate_withValidErrorClass_returnsTemplate() {
        String result = errorClassesJsonReader.getMessageTemplate("mainErrorClass");
        assertEquals("Expected message template", result);
    }

    @Test
    void getMessageTemplate_withInvalidErrorClass_throwsException() {
        assertThrows(Exception.class, () -> errorClassesJsonReader.getMessageTemplate("invalidErrorClass"));
    }

    @Test
    void getSqlState_withValidErrorClass_returnsSqlState() {
        String result = errorClassesJsonReader.getSqlState("mainErrorClass");
        assertEquals("Expected SQL state", result);
    }

    @Test
    void getSqlState_withInvalidErrorClass_returnsNull() {
        String result = errorClassesJsonReader.getSqlState("invalidErrorClass");
        assertNull(result);
    }
}