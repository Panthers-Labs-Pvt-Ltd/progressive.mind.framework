package com.progressive.minds.chimera.common.exception;

/**
 * The Class DatabaseException.
 */
public class DatabaseException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 23489572345789L;

    /**
     * Constructor.
     * 
     * @param message Message
     * @param e Cause
     */
    public DatabaseException(String message, Throwable e) {
        super(message, e);
    }
}
