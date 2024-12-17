package com.progressive.minds.chimera.core.datalineage.models.Transport;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class ConfigReaderTest {

    @Test
    void getEnvironmentProperties() throws IOException {
        configReader.getEnvironmentProperties("kafka");
    }

}