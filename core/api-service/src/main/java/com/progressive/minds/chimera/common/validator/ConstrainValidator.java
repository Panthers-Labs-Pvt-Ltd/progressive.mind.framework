package com.progressive.minds.chimera.common.validator;

import com.progressive.minds.chimera.common.exception.FailedValidationException;
import com.progressive.minds.chimera.common.exception.UserException;

public interface ConstrainValidator<T> {

  void validate(T obj) throws FailedValidationException, UserException;
}