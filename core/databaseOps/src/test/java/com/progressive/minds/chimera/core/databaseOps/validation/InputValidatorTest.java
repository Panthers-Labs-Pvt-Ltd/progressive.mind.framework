package com.progressive.minds.chimera.core.databaseOps.validation;

import com.progressive.minds.chimera.core.databaseOps.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class InputValidatorTest {
    @BeforeEach
    void setUp() {
        System.setProperty("CHIMERA_EXE_ENV", "UnitTest");
    }

    @Test
    void testValidateName_ThrowsException_WhenNameIsNull() {
        assertThrows(ValidationException.class, () -> InputValidator.validateName(null));
    }

    @Test
    void testValidateName_ThrowsException_WhenNameIsEmpty() {
        assertThrows(ValidationException.class, () -> InputValidator.validateName(""));
    }

    @Test
    void testValidateEmail_ThrowsException_WhenEmailIsInvalid() {
        assertThrows(ValidationException.class, () -> InputValidator.validateEmail("invalid-email"));
    }

    @Test
    void testValidateEmail_ThrowsException_WhenEmailIsNull() {
        assertThrows(ValidationException.class, () -> InputValidator.validateEmail(null));
    }
}
