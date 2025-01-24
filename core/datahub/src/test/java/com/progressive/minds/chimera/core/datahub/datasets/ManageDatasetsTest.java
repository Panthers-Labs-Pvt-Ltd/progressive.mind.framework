package com.progressive.minds.chimera.core.datahub.datasets;

import com.ibm.icu.impl.data.ResourceReader;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ManageDatasetsTest {

    @Test
    void createDataset() throws Exception {
        String SchemaFile ="/home/manish/Chimera2.0/chimera/core/datahub/src/test/resources/DatasetJsonSchemaExample.json";
        String fileContent = new String(ResourceReader.class.getClassLoader()
                .getResourceAsStream("DatasetJsonSchemaExample.json").readAllBytes(), StandardCharsets.UTF_8);
        String[]  PrimaryKeys = {"FieldName1","FieldName2"};
        String[]  datasetTags = {"My Test Dataset","Initial"};
        Map<String, String> customProperties = new HashMap<>();
        customProperties.put("Property1", "Value1");
        customProperties.put("Property2", "Value2");

        ManageDatasets.DatasetsRecords datasetsInfo = new ManageDatasets.DatasetsRecords("Postgres",
                "ManageDatasets.DatasetsRecords", "My test dataset description", "Dev",
                SchemaFile,"json","http://mydatasetc.com","dataset Qualified Name",
                PrimaryKeys,null,datasetTags,"http://mydatasetc.com/index.html",
                customProperties, fileContent, "Manish");

        System.out.println(ManageDatasets.createDataset(datasetsInfo));
    }
}