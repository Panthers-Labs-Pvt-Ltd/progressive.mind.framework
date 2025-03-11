package com.progressive.minds.chimera.core.api_service.common.exception;

import com.progressive.minds.chimera.core.api_service.common.dto.ExceptionMessage;
import com.progressive.minds.chimera.core.api_service.common.dto.GenericResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Custom exception for handling user-related errors.
 */
@Getter
public class UserException extends RuntimeException {

  private final GenericResponse error;
  private final HttpStatus status;

  /**
   * Constructor.
   *
   * @param exceptionMessage
   * @param status
   * @param error
   */
  public UserException(
      final ExceptionMessage exceptionMessage,
      final HttpStatus status,
      final GenericResponse error) {
    super(exceptionMessage.getDescription());
    this.status = status;
    this.error = error;
  }
}
