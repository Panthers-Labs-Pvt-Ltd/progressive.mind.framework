package com.progressive.minds.chimera.core.datahub.pipeline;

import com.ibm.icu.impl.data.ResourceReader;
import com.progressive.minds.chimera.core.datahub.datasets.DatasetManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ManagePipelineTest {

    @Test
    void createDataPipeline() throws Exception {
        String fileContent = new String(Objects.requireNonNull(ResourceReader.class.getClassLoader()
                .getResourceAsStream("pipeline.yaml")).readAllBytes(), StandardCharsets.UTF_8);
        ManagePipeline.createDataPipeline(fileContent);
    }
}