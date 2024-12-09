package com.progressive.minds.chimera;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class OpenTelemetryConfigTest {

    @Test
    void testInitializeTelemetry() {
        assertNotNull(OpenTelemetryConfig.initializeTelemetry());
    }
}