package com.progressive.minds.chimera.core.api_service.common.exception;

import com.progressive.minds.chimera.core.api_service.common.dto.ExceptionMessage;
import com.progressive.minds.chimera.core.api_service.common.dto.GenericResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Exception to indicate validation failures.
 */
@Getter
public class FailedValidationException extends RuntimeException {

  private final GenericResponse error;
  private final HttpStatus status;

  /**
   * Constructor.
   *
   * @param exceptionMessage
   * @param status
   * @param error
   */
  public FailedValidationException(
      final ExceptionMessage exceptionMessage,
      final HttpStatus status,
      final GenericResponse error) {
    super(exceptionMessage.getDescription());
    this.status = status;
    this.error = error;
  }

}
