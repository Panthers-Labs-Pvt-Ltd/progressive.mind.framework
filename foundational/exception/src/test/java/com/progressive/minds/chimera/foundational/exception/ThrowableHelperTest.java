package com.progressive.minds.chimera.foundational.exception;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ThrowableHelperTest {

    @Test
    void getMessage_withValidErrorClassAndParameters_returnsFormattedMessage() {
        Map<String, String> messageParameters = Map.of("param1", "value1");
        String result = ThrowableHelper.getMessage("ERROR_CLASS", messageParameters);
        assertEquals("[ERROR_CLASS] Some formatted message with value1", result);
    }

    @Test
    void getMessage_withEmptyContext_returnsMessageWithoutContext() {
        Map<String, String> messageParameters = Map.of("param1", "value1");
        String result = ThrowableHelper.getMessage("ERROR_CLASS", messageParameters, "");
        assertEquals("[ERROR_CLASS] Some formatted message with value1", result);
    }

    @Test
    void getMessage_withNonEmptyContext_returnsMessageWithContext() {
        Map<String, String> messageParameters = Map.of("param1", "value1");
        String result = ThrowableHelper.getMessage("ERROR_CLASS", messageParameters, "Context info");
        assertEquals("[ERROR_CLASS] Some formatted message with value1\nContext info", result);
    }

    @Test
    void getSqlState_withValidErrorClass_returnsSqlState() {
        String result = ThrowableHelper.getSqlState("ERROR_CLASS");
        assertEquals("08001", result);
    }

    @Test
    void isInternalError_withInternalErrorClass_returnsTrue() {
        assertTrue(ThrowableHelper.isInternalError("INTERNAL_ERROR"));
    }

    @Test
    void isInternalError_withNonInternalErrorClass_returnsFalse() {
        assertFalse(ThrowableHelper.isInternalError("NON_INTERNAL_ERROR"));
    }

//    @Test
//    void getMessage_withThrowableAndPrettyFormat_returnsThrowableMessage() {
//        Throwable throwable = new Throwable("Error message");
//        String result = ThrowableHelper.getMessage(throwable, ErrorMessageFormat.PRETTY);
//        assertEquals("Error message", result);
//    }
//
//    @Test
//    void getMessage_withThrowableAndMinimalFormat_returnsJsonMessage() {
//        Throwable throwable = new Exception("Error message");
//        String result = ThrowableHelper.getMessage(throwable, ErrorMessageFormat.MINIMAL);
//        assertTrue(result.contains("\"errorClass\":\"java.lang.Throwable\""));
//        assertTrue(result.contains("\"message\":\"Error message\""));
//    }
//
//    @Test
//    void getMessage_withThrowableAndStandardFormat_returnsJsonMessageWithTemplate() {
//        Throwable throwable = new Throwable("Error message");
//        String result = ThrowableHelper.getMessage(throwable, ErrorMessageFormat.STANDARD);
//        assertTrue(result.contains("\"errorClass\":\"java.lang.Throwable\""));
//        assertTrue(result.contains("\"message\":\"Error message\""));
//        assertTrue(result.contains("\"messageTemplate\":\"Some template\""));
//    }
//
//    @Test
//    void getMessage_withThrowable_returnsJsonMessage() {
//        Throwable throwable = new Throwable("Error message");
//        String result = ThrowableHelper.getMessage(throwable);
//        assertTrue(result.contains("\"errorClass\":\"java.lang.Throwable\""));
//        assertTrue(result.contains("\"message\":\"Error message\""));
//    }
}