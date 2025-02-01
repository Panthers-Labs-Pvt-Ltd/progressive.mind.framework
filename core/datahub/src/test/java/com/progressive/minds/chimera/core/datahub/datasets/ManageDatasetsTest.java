package com.progressive.minds.chimera.core.datahub.datasets;

import com.ibm.icu.impl.data.ResourceReader;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

class ManageDatasetsTest {

    @Test
    void createDataset() throws Exception {
        String fileContent = new String(Objects.requireNonNull(ResourceReader.class.getClassLoader()
                .getResourceAsStream("ManageDataset.yaml")).readAllBytes(), StandardCharsets.UTF_8);
        ManageDatasets.createDataset(fileContent, "manish");

    }

    @Test
    void modifyDataset() throws Exception {
        ManageDatasets.modifyDatasetProperties();
    }
    }