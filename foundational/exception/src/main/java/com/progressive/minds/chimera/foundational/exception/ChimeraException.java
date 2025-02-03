package com.progressive.minds.chimera.foundational.exception;

import java.util.HashMap;
import java.util.Map;

public class ChimeraException extends RuntimeException implements Throwable {

    private String errorClass;
    private Map<String, String> messageParameters;

    // Primary Constructor
    public ChimeraException(String message, Throwable cause, String errorClass, Map<String, String> messageParameters) {
        super(message, (java.lang.Throwable) cause);
        this.errorClass = errorClass;
        this.messageParameters = messageParameters != null ? messageParameters : new HashMap<>();
    }

    // Constructor with message and cause
    public ChimeraException(String message, Throwable cause) {
        this(message, cause, null, null);
    }

    // Constructor with message only
    public ChimeraException(String message, String errorClass, Map<String, String> messageParameters) {
        this(message, (Throwable) null, null, null);
        this.errorClass = errorClass;
        this.messageParameters = messageParameters;
    }

    // Constructor with errorClass, messageParameters, and cause
    public ChimeraException(String errorClass, Map<String, String> messageParameters, Throwable cause) {
        this(ThrowableHelper.getMessage(errorClass, messageParameters), cause, errorClass, messageParameters);
    }

    // Constructor with errorClass, messageParameters, cause, and summary
    public ChimeraException(String errorClass, Map<String, String> messageParameters, Throwable cause, String summary) {
        this(ThrowableHelper.getMessage(errorClass, messageParameters, summary), cause, errorClass, messageParameters);
    }

    @Override
    public Map<String, String> getMessageParameters() {
        return messageParameters;
    }

    @Override
    public String getErrorClass() {
        return errorClass;
    }

    // Static Factory Methods
    public static ChimeraException internalError(String msg) {
        Map<String, String> params = new HashMap<>();
        params.put("message", msg);
        return new ChimeraException("INTERNAL ERROR", params, null);
    }

    public static ChimeraException internalError(String msg, Throwable cause) {
        Map<String, String> params = new HashMap<>();
        params.put("message", msg);
        return new ChimeraException("INTERNAL ERROR", params, cause);
    }
}
