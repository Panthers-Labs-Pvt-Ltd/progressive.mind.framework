package com.progressive.minds.chimera.core.api_service.common.exception;

import com.progressive.minds.chimera.core.api_service.common.dto.ExceptionMessage;
import com.progressive.minds.chimera.core.api_service.common.dto.GenericResponse;
import com.progressive.minds.chimera.core.api_service.common.dto.RemoteServiceError;
import java.io.Serial;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * An exception threw when a async process returns a timeout error. It's eligible for fallback and
 * the original exception is wrapped in a HystrixRuntimeException
 */
@Getter
public class RemoteServiceTimeoutException extends RemoteServiceError {

  @Serial
  private static final long serialVersionUID = 1L;
  private final GenericResponse error;
  private final HttpStatus status;

  /**
   * Constructor.
   *
   * @param exceptionMessage
   * @param status
   * @param error
   */
  public RemoteServiceTimeoutException(final ExceptionMessage exceptionMessage,
      final HttpStatus status, final GenericResponse error) {
    super(exceptionMessage.getDescription());
    this.status = status;
    this.error = error;
  }
}
