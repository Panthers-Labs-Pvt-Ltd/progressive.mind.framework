package com.progressive.minds.chimera.core.datahub.pipeline;

import com.ibm.icu.impl.data.ResourceReader;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

class ManagePipelineTest {

    @Test
    void createDataPipeline() throws Exception {
        String fileContent = new String(Objects.requireNonNull(ResourceReader.class.getClassLoader()
                .getResourceAsStream("ManagePipeline.yaml")).readAllBytes(), StandardCharsets.UTF_8);
        ManagePipeline.createDataPipeline(fileContent);
    }
}