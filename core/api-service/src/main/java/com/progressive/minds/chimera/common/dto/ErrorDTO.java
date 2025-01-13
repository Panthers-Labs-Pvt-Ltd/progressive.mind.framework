package com.progressive.minds.chimera.common.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * The Class ErrorDetailDTO. It's used to return standard information about the error to the
 * consumer service when an exception occurs.
 */
@Data
@Builder
@AllArgsConstructor
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDTO implements Serializable {

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -5767906253263397432L;

  /**
   * Time and date the error was thrown.
   */
  @Builder.Default
  private LocalDateTime dateTime = LocalDateTime.now();

  /**
   * Error code.
   */
  private String code;

  /**
   * Error message.
   */
  private String message;

  /**
   * Error description.
   */
  private String description;

  /**
   * The name of the service that threw this error.
   */
  private String serviceName;

  /**
   * Indicates if the error is caused by a bad request from the client.
   */
  @Builder.Default
  private Boolean isBadRequest = false;

  /**
   * The trace ID.
   */
  private String traceId;

  /**
   * List of errors caused by field validations.
   */
  @Builder.Default
  private List<FieldErrorDTO> fieldErrors = new ArrayList<>();

  /**
   * The nested error that caused this error.
   */
  private ErrorDTO nestedError;
}

