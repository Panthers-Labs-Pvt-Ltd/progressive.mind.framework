package com.progressive.minds.chimera.core.api_service.common.dto;

import org.springframework.http.HttpStatus;

/**
 * Implemented by all exceptions that handles remote service errors
 */
public abstract class RemoteServiceError extends RuntimeException {

  public RemoteServiceError(String message) {
    super(message);
  }

  /**
   * Gets status.
   *
   * @return HttpStatus status
   */
  public abstract HttpStatus getStatus();

  /**
   * Gets error dto.
   *
   * @return ErrorDTO error dto
   */
  public abstract GenericResponse getError();

  @Override
  public String toString() {
    StringBuilder message = new StringBuilder(super.toString());
    message.append(". Response Status is ").append(getStatus());
    message.append(" and the error is ").append(getError());
    return message.toString();
  }
}
