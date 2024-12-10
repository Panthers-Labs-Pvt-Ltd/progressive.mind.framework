package com.progressive.minds.chimera.foundational.exception;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ErrorSubInfoTest {

    @Test
    void getMessageTemplate_returnsJoinedMessage() {
        ErrorSubInfo errorSubInfo = new ErrorSubInfo();
        errorSubInfo.setMessage(List.of("SubError 1", "SubError 2"));
        System.out.print(errorSubInfo.getMessageTemplate());
        assertEquals("SubError 1\nSubError 2", errorSubInfo.getMessageTemplate());
    }

    @Test
    void getMessage_returnsMessageList() {
        ErrorSubInfo errorSubInfo = new ErrorSubInfo();
        List<String> messages = List.of("SubError 1", "SubError 2");
        errorSubInfo.setMessage(messages);
        assertEquals(messages, errorSubInfo.getMessage());
    }

    @Test
    void getMessageTemplate_withEmptyMessageList_returnsEmptyString() {
        ErrorSubInfo errorSubInfo = new ErrorSubInfo();
        errorSubInfo.setMessage(List.of());
        assertEquals("", errorSubInfo.getMessageTemplate());
    }

    @Test
    void getMessageTemplate_withNullMessageList_returnsEmptyString() {
        ErrorSubInfo errorSubInfo = new ErrorSubInfo();
        errorSubInfo.setMessage(null);
        assertEquals("", errorSubInfo.getMessageTemplate());
    }
}