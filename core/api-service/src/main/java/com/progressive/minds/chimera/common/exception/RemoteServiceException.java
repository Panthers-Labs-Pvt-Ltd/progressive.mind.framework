package com.progressive.minds.chimera.common.exception;

import com.progressive.minds.chimera.common.dto.ExceptionMessage;
import com.progressive.minds.chimera.common.dto.GenericResponse;
import com.progressive.minds.chimera.common.dto.RemoteServiceError;
import org.springframework.http.HttpStatus;


import lombok.Getter;

/**
 * An exception threw because an application or service failure.
 * It's eligible for fallback and the original exception is wrapped in a HystrixRuntimeException
 */
@Getter
public class RemoteServiceException extends RemoteServiceError {
    private static final long serialVersionUID = 3111443782817785777L;
    private final GenericResponse error;
    private final HttpStatus status;

    /**
     * Constructor.
     * 
     * @param exceptionMessage
     * @param status
     * @param error
     */
    public RemoteServiceException(
        final ExceptionMessage exceptionMessage,
        final HttpStatus status,
        final GenericResponse error) {
        super(exceptionMessage.getDescription());
        this.status = status;
        this.error = error;
    }
}
