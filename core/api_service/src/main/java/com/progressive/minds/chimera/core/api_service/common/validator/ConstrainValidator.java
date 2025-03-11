package com.progressive.minds.chimera.core.api_service.common.validator;

import com.progressive.minds.chimera.core.api_service.common.exception.FailedValidationException;
import com.progressive.minds.chimera.core.api_service.common.exception.UserException;

public interface ConstrainValidator<T> {

  void validate(T obj) throws FailedValidationException, UserException;
}