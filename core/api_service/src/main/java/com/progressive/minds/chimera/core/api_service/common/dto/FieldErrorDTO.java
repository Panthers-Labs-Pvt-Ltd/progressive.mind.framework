package com.progressive.minds.chimera.core.api_service.common.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Description of an error caused by field validation
 */
@Data
@Builder
@AllArgsConstructor
public class FieldErrorDTO implements Serializable {

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = -5767906253263497432L;

  /**
   * The field error code
   */
  private String code;

  /**
   * The name of the field with the error
   */
  private String field;

  /**
   * The field error message
   */
  private String message;
}
