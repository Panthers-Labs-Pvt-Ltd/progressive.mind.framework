package com.progressive.minds.chimera.foundational.exception;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ErrorInfoTest {

    @Test
    void getMessageTemplate_returnsJoinedMessage() {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setMessage(List.of("Error 1", "Error 2"));
        assertEquals("Error 1\nError 2", errorInfo.getMessageTemplate());
    }

    @Test
    void getMessage_returnsMessageList() {
        ErrorInfo errorInfo = new ErrorInfo();
        List<String> messages = List.of("Error 1", "Error 2");
        errorInfo.setMessage(messages);
        assertEquals(messages, errorInfo.getMessage());
    }

    @Test
    void getSubClass_returnsSubClassMap() {
        ErrorInfo errorInfo = new ErrorInfo();
        Map<String, ErrorSubInfo> subClassMap = Map.of("key1", new ErrorSubInfo(), "key2", new ErrorSubInfo());
        errorInfo.setSubClass(subClassMap);
        assertEquals(subClassMap, errorInfo.getSubClass());
    }

    @Test
    void getSqlState_returnsSqlState() {
        ErrorInfo errorInfo = new ErrorInfo();
        String sqlState = "08001";
        errorInfo.setSqlState(sqlState);
        assertEquals(sqlState, errorInfo.getSqlState());
    }

    @Test
    void getMessageTemplate_withEmptyMessageList_returnsEmptyString() {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setMessage(List.of());
        assertEquals("", errorInfo.getMessageTemplate());
    }

    @Test
    void getMessageTemplate_withNullMessageList_returnsEmptyString() {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setMessage(null);
        assertEquals("", errorInfo.getMessageTemplate());
    }
}