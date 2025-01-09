package com.progressive.minds.chimera.common;


import com.progressive.minds.chimera.common.dto.ErrorDTO;
import com.progressive.minds.chimera.common.dto.ExceptionMessage;
import com.progressive.minds.chimera.common.dto.GenericResponse;
import com.progressive.minds.chimera.common.exception.RemoteServiceException;
import com.progressive.minds.chimera.common.exception.RemoteServiceTimeoutException;
import com.progressive.minds.chimera.common.exception.UserException;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import jdk.jshell.spi.ExecutionControl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.io.IOException;
import java.net.BindException;


/**
 * Handles the error response depending on every handled exception
 */

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String X_TRACE_ID = "X_TRACE_ID";
    public static final String MDC_UUID_TOKEN_KEY = "Log4UUIDFilter.UUID";

    private static final ChimeraLogger log = ChimeraLoggerFactory.getLogger(GlobalExceptionHandler.class);
    @Value("${spring.application.name}")
    private String appName;


    /**
     * Generic handle for all unknown errors
     * 
     * @param exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    private ResponseEntity<ErrorDTO> handleApplicationException(final Exception exception) {
        log.logError("Unexpected error", exception);
        return new ResponseEntity<>(buildErrorDTO(ExceptionMessage.UNEXPECTED_ERROR.toString(), exception.getMessage(), true),
            getXTRaceHeader(),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Generic handle for all illegal arguments errors
     * 
     * @param exception
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    private ResponseEntity<ErrorDTO> handleIllegalArgumentException(
        final IllegalArgumentException exception) {
        log.logWarning(exception.getMessage(), exception);
        return new ResponseEntity<>(
            buildErrorDTO(ExceptionMessage.ILLEGAL_ARGUMENT.toString(), exception.getMessage(), true),
            getXTRaceHeader(),
            HttpStatus.BAD_REQUEST);
    }

    /**
     * Generic handle for all validation exceptions errors
     *
     * @param exception the exception
     * @return the object
     */
    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        MissingServletRequestPartException.class,
        MissingServletRequestParameterException.class,
        BindException.class,
        HttpMessageNotReadableException.class,
        TypeMismatchException.class,
        IOException.class
    })
    @ResponseBody
    public ResponseEntity<ErrorDTO> badRequestFormatException(final Exception exception) {
        log.logWarning(exception.getMessage(), exception);
        return new ResponseEntity<>(
            buildErrorDTO(ExceptionMessage.ILLEGAL_ARGUMENT.toString(), exception.getMessage(), true),
            getXTRaceHeader(),
            HttpStatus.BAD_REQUEST);
    }

    /**
     * Generic handle for all user exception errors
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(ExecutionControl.UserException.class)
    @ResponseBody
    private ResponseEntity<ErrorDTO> handleUserException(
        final ExecutionControl.UserException exception) {
        log.logWarning(exception.getMessage(), exception);
        return new ResponseEntity<>(
            buildErrorDTO(ExceptionMessage.ILLEGAL_ARGUMENT.toString(), exception.getMessage(), true),
            getXTRaceHeader(),
            HttpStatus.BAD_REQUEST);
    }

    /**
     * Generic handle for all service errors from other services
     *
     * @param exception the exception
     * @return the response entity
     */
    @ExceptionHandler(RemoteServiceException.class)
    @ResponseBody
    private ResponseEntity<ErrorDTO> handleRemoteServiceException(final RemoteServiceException exception) {
        log.logError(exception.getMessage(), exception);
        GenericResponse nestedError = exception.getError();
        String code = nestedError == null ? ExceptionMessage.REMOTE_SERVICE_ERROR.toString() : exception.getLocalizedMessage();

        return new ResponseEntity<>(
            buildErrorDTO(code, exception.getMessage(), false),
            getXTRaceHeader(),
            exception.getStatus());
    }

    /**
     * Generic handle for all timeout errors from async operations of another services
     *
     * @param exception the exception
     * @return the response entity
     */
    @ExceptionHandler(RemoteServiceTimeoutException.class)
    @ResponseBody
    private ResponseEntity<ErrorDTO> handleRemoteServiceTimeoutException(
        final RemoteServiceTimeoutException exception) {
        log.logError(exception.getMessage(), exception);
        GenericResponse nestedError = exception.getError();
        String code = nestedError == null ? ExceptionMessage.REMOTE_SERVICE_TIMEOUT_ERROR.toString() : exception.getLocalizedMessage();

        return new ResponseEntity<>(
            buildErrorDTO(code, exception.getMessage(), false),
            getXTRaceHeader(),
            exception.getStatus());
    }

    /**
     * Generic handle for all user exception errors
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(UserException.class)
    @ResponseBody
    private ResponseEntity<ErrorDTO> handleUserException(final UserException exception) {
        log.logError( exception.getMessage(), exception);
        GenericResponse nestedError = exception.getError();
        String code = nestedError == null ? ExceptionMessage.REMOTE_SERVICE_TIMEOUT_ERROR.toString() : exception.getLocalizedMessage();

        return new ResponseEntity<>(
                buildErrorDTO(code, exception.getMessage(), false),
                getXTRaceHeader(),
                exception.getStatus());
    }


    private ErrorDTO buildErrorDTO(String code, String message, boolean isBadRequest) {
        return ErrorDTO.builder()
            .code(code)
            .message(message)
            .serviceName(appName)
            .isBadRequest(isBadRequest)
            .traceId(MDC.get(MDC_UUID_TOKEN_KEY))
            .build();
    }

    /**
     * Gets the XT race header.
     *
     * @return the XT race header
     */
    private HttpHeaders getXTRaceHeader() {
        final HttpHeaders headers = new HttpHeaders();
        final String traceId = MDC.get(MDC_UUID_TOKEN_KEY);
        if (StringUtils.isNotEmpty(traceId)) {
            headers.add(X_TRACE_ID, traceId);
        }
        return headers;
    }
}
