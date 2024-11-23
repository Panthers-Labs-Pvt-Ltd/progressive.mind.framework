package com.progressive.minds.chimera.core.databaseOps.validation;
import com.progressive.minds.chimera.core.databaseOps.exception.ValidationException;

public class InputValidator {

    public static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Name cannot be null or empty.");
        }
    }

    public static void validateEmail(String email) {
        if (email == null || !email.matches("^[\\w-\\.]+@[\\w-\\.]+\\.[a-zA-Z]{2,}$")) {
            throw new ValidationException("Invalid email format.");
        }
    }
}