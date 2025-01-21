package com.progressive.minds.chimera.core.datahub.datasets;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManageDatasetsTest {

    @Test
    void createDataset() throws Exception {
        String SchemaFile ="/home/manish/Chimera2.0/chimera/core/datahub/src/test/resources/DatasetYmlSchemaExample.yml";
        ManageDatasets.DatasetsRecords datasetsInfo = new ManageDatasets.DatasetsRecords("Postgres",
                "mytestdataset", "My test dataset description", "Dev",
                SchemaFile,"yaml",null,null,
                null,null,null,null,null, "Manish");

        ManageDatasets.createDataset(datasetsInfo);
    }
}