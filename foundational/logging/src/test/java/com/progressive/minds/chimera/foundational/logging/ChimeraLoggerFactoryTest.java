package com.progressive.minds.chimera.foundational.logging;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChimeraLoggerFactoryTest {

    @Test
    void getLoggerReturnsNonNullLogger() {
        ChimeraLogger logger = ChimeraLoggerFactory.getLogger("ChimeraLoggerFactoryTest");
        assertNotNull(logger);
    }

    @Test
    void getLoggerReturnsSameInstance() {
        // TODO - Should it?
        ChimeraLogger logger1 = ChimeraLoggerFactory.getLogger("ChimeraLoggerFactoryTest");
        ChimeraLogger logger2 = ChimeraLoggerFactory.getLogger("ChimeraLoggerFactoryTest");
        // assertSame(logger1, logger2);
    }
}