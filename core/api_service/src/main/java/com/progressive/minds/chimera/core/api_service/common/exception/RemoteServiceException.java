package com.progressive.minds.chimera.core.api_service.common.exception;

import com.progressive.minds.chimera.core.api_service.common.dto.ExceptionMessage;
import com.progressive.minds.chimera.core.api_service.common.dto.GenericResponse;
import com.progressive.minds.chimera.core.api_service.common.dto.RemoteServiceError;
import java.io.Serial;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * An exception threw because an application or service failure. It's eligible for fallback and the
 * original exception is wrapped in a HystrixRuntimeException
 */
@Getter
public class RemoteServiceException extends RemoteServiceError {

  @Serial
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
