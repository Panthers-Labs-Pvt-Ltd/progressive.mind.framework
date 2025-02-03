package com.progressive.minds.chimera.core.datalineage.models.Transport;

import com.progressive.minds.chimera.core.datalineage.config.configReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class ConfigReaderTest {

    @Test
    void getEnvironmentProperties() throws IOException {
        configReader.getEnvironmentProperties("kafka", "MNA",null);
    }

}