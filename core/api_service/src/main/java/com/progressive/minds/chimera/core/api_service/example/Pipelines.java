package com.progressive.minds.chimera.core.api_service.example;

public class Pipelines {
    public static final String GET_PIPELINE_DETAILS_RESPONSE = "{\"pipelineName\":\"Test_Pipeline\",\"pipelineDescription\":\"test Pipeline\",\"processMode\":\"Batch\",\"tags\":\"ETL, Chimera\",\"orgHierName\":\"Chimera\",\"activeFlag\":\"N\",\"createdTimestamp\":\"2025-01-06T13:49:41.220+00:00\",\"createdBy\":\"PK\",\"updatedTimestamp\":\"2025-01-08T12:01:36.216+00:00\",\"updatedBy\":\"PK\",\"org\":{\"orgHierName\":\"Chimera\",\"orgTypeName\":\"Panther Labs\",\"parentOrgName\":null,\"cooOwner\":\"Abhinav\",\"opsLead\":\"Prashant\",\"techLead\":\"Manish\",\"busOwner\":\"Mahim\",\"orgDesc\":\"Chimera Product\",\"orgEmail\":\"chimera@pantherlabs.com\",\"orgCi\":\"Sample\",\"userField1\":null,\"userField2\":null,\"userField3\":null,\"userField4\":null,\"userField5\":null,\"createdTimestamp\":\"2025-01-06T11:51:43.334+00:00\",\"createdBy\":\"PK\",\"updatedTimestamp\":\"2025-01-16T14:36:13.008+00:00\",\"updatedBy\":\"PK\",\"activeFlag\":\"Y\"},\"extractMetadata\":[{\"pipelineName\":\"Test_Pipeline\",\"sequenceNumber\":1,\"extractSourceType\":\"Files\",\"extractSourceSubType\":\"Csv\",\"dataframeName\":\"file_df\",\"sourceConfiguration\":\"{\\\"delimiter\\\":\\\",\\\",\\\"header\\\":true}\",\"predecessorSequences\":\"0\",\"successorSequences\":\"2\",\"rowFilter\":null,\"columnFilter\":null,\"dataSourceConnectionName\":null,\"createdTimestamp\":\"2025-01-08T18:29:05.150+00:00\",\"createdBy\":\"PK\",\"updatedTimestamp\":null,\"updatedBy\":null,\"activeFlag\":\"Y\",\"fileMetadata\":{\"fileName\":\"test.csv\",\"filePath\":\"/tmp/test.csv\",\"schemaPath\":\"/tmp/schema.json\",\"sizeInByte\":1000,\"compressionType\":\"GZIP\"},\"relationalMetadata\":null,\"noSqlMetadata\":null,\"streamMetadata\":null,\"dataSource\":{\"dataSourceType\":\"Files\",\"dataSourceSubType\":\"Csv\",\"description\":\"File Format CSV\",\"dataSourceTemplate\":null,\"defaultReadConfig\":null,\"defaultWriteConfig\":null,\"createdTimestamp\":\"2025-01-06T11:52:42.278+00:00\",\"createdBy\":\"PK\",\"updatedTimestamp\":null,\"updatedBy\":null,\"activeFlag\":\"Y\"},\"dataSourceConnection\":null},{\"pipelineName\":\"Test_Pipeline\",\"sequenceNumber\":2,\"extractSourceType\":\"NoSql\",\"extractSourceSubType\":\"Mongodb\",\"dataframeName\":\"input_df\",\"sourceConfiguration\":\"{\\\"key\\\":\\\"value\\\"}\",\"predecessorSequences\":\"0\",\"successorSequences\":\"2\",\"rowFilter\":null,\"columnFilter\":null,\"dataSourceConnectionName\":\"Chimera_Postgres_Local_connection\",\"createdTimestamp\":\"2025-01-08T18:28:09.808+00:00\",\"createdBy\":\"admin\",\"updatedTimestamp\":null,\"updatedBy\":null,\"activeFlag\":\"Y\",\"fileMetadata\":null,\"relationalMetadata\":null,\"noSqlMetadata\":{\"collection\":\"myCollection\",\"partitioner\":\"defaultPartitioner\"},\"streamMetadata\":null,\"dataSource\":{\"dataSourceType\":\"NoSql\",\"dataSourceSubType\":\"Mongodb\",\"description\":\"Mongo DB\",\"dataSourceTemplate\":null,\"defaultReadConfig\":null,\"defaultWriteConfig\":null,\"createdTimestamp\":\"2025-01-06T11:52:42.278+00:00\",\"createdBy\":\"PK\",\"updatedTimestamp\":null,\"updatedBy\":null,\"activeFlag\":\"Y\"},\"dataSourceConnection\":{\"dataSourceConnectionName\":\"Chimera_Postgres_Local_connection\",\"dataSourceType\":\"Relational\",\"dataSourceSubType\":\"Postgres\",\"authenticationType\":\"USER_PASSWORD\",\"authenticationData\":\"{\\\"username\\\":\\\"chimera\\\",\\\"password\\\":\\\"chimera123\\\"}\",\"connectionMetadata\":\"jdbc:postgresql://localhost:5432/chimera_db\",\"userReadConfig\":\"updated via API\",\"userWriteConfig\":\"updated via API\",\"description\":\"Connection details of chimera_db installed locally on postgres\",\"createdTimestamp\":\"2025-01-06T13:57:22.977+00:00\",\"createdBy\":\"PK\",\"updatedTimestamp\":\"2025-01-16T09:41:12.532+00:00\",\"updatedBy\":\"PK\",\"activeFlag\":\"Y\"}}],\"transformMetadata\":[],\"persistMetadata\":[{\"pipelineName\":\"Test_Pipeline\",\"sequenceNumber\":1,\"sinkType\":\"Relational\",\"sinkSubType\":\"Postgres\",\"predecessorSequences\":null,\"successorSequences\":null,\"dataSourceConnectionName\":\"Chimera_Postgres_Local_connection\",\"partitionKeys\":null,\"targetSql\":\"select * from dataframe\",\"sinkConfiguration\":null,\"sortColumns\":null,\"dedupColumns\":null,\"createdTimestamp\":\"2025-02-24T08:40:02.076+00:00\",\"createdBy\":\"PK\",\"updatedTimestamp\":null,\"updatedBy\":null,\"activeFlag\":\"Y\",\"filePersistMetadataTable\":null,\"noSqlPersistMetadataTable\":null,\"relationalPersistMetadataTable\":{\"databaseName\":\"chimera_test\",\"tableName\":\"sampleTable\",\"schemaName\":null},\"streamPersistMetadataTable\":null,\"dataSource\":{\"dataSourceType\":\"Relational\",\"dataSourceSubType\":\"Postgres\",\"description\":\"Postgres Database\",\"dataSourceTemplate\":null,\"defaultReadConfig\":null,\"defaultWriteConfig\":null,\"createdTimestamp\":\"2025-01-06T11:52:42.278+00:00\",\"createdBy\":\"PK\",\"updatedTimestamp\":null,\"updatedBy\":null,\"activeFlag\":\"Y\"},\"dataSourceConnection\":{\"dataSourceConnectionName\":\"Chimera_Postgres_Local_connection\",\"dataSourceType\":\"Relational\",\"dataSourceSubType\":\"Postgres\",\"authenticationType\":\"USER_PASSWORD\",\"authenticationData\":\"{\\\"username\\\":\\\"chimera\\\",\\\"password\\\":\\\"chimera123\\\"}\",\"connectionMetadata\":\"jdbc:postgresql://localhost:5432/chimera_db\",\"userReadConfig\":\"updated via API\",\"userWriteConfig\":\"updated via API\",\"description\":\"Connection details of chimera_db installed locally on postgres\",\"createdTimestamp\":\"2025-01-06T13:57:22.977+00:00\",\"createdBy\":\"PK\",\"updatedTimestamp\":\"2025-01-16T09:41:12.532+00:00\",\"updatedBy\":\"PK\",\"activeFlag\":\"Y\"}}]}";
    public static final String GET_ALL_PIPELINES_RESPONSE = "[\n" +
      "    {\n" +
      "        \"pipelineName\": \"Test_Pipeline\",\n" +
      "        \"pipelineDescription\": \"test Pipeline\",\n" +
      "        \"processMode\": \"Batch\",\n" +
      "        \"tags\": \"ETL, Chimera\",\n" +
      "        \"orgHierName\": \"Chimera\",\n" +
      "        \"activeFlag\": \"N\",\n" +
      "        \"createdTimestamp\": \"2025-01-06T13:49:41.220+00:00\",\n" +
      "        \"createdBy\": \"PK\",\n" +
      "        \"updatedTimestamp\": \"2025-01-08T12:01:36.216+00:00\",\n" +
      "        \"updatedBy\": \"PK\"\n" +
      "    },\n" +
      "    {\n" +
      "        \"pipelineName\": \"Test_Pipeline_API\",\n" +
      "        \"pipelineDescription\": \"test Pipeline via Postman\",\n" +
      "        \"processMode\": \"Batch\",\n" +
      "        \"tags\": \"ETL, Chimera\",\n" +
      "        \"orgHierName\": \"Chimera\",\n" +
      "        \"activeFlag\": null,\n" +
      "        \"createdTimestamp\": null,\n" +
      "        \"createdBy\": \"PK\",\n" +
      "        \"updatedTimestamp\": null,\n" +
      "        \"updatedBy\": null\n" +
      "    },\n" +
      "    {\n" +
      "        \"pipelineName\": \"Test_Pipeline_API_1\",\n" +
      "        \"pipelineDescription\": \"test Pipeline via Postman\",\n" +  
      "        \"processMode\": \"Batch\",\n" +
      "        \"tags\": \"ETL, Chimera\",\n" +
      "        \"orgHierName\": \"Chimera\",\n" +
      "        \"activeFlag\": null,\n" +
      "        \"createdTimestamp\": \"2025-01-10T16:37:25.427+00:00\",\n" +
      "        \"createdBy\": \"PK\",\n" +
      "        \"updatedTimestamp\": null,\n" +
      "        \"updatedBy\": null\n" +
      "    },\n" +
      "    {\n" +
      "        \"pipelineName\": \"Test_Pipeline_Postgres\",\n" +
      "        \"pipelineDescription\": \"This is a test pipeline to test data ingestion from a postgres table.\",\n" +
      "        \"processMode\": \"Batch\",\n" +
      "        \"tags\": \"Test, DataIngestion, Postgres\",\n" +
      "        \"orgHierName\": \"Chimera\",\n" +
      "        \"activeFlag\": \"Y\",\n" +
      "        \"createdTimestamp\": \"2025-02-07T12:16:56.755+00:00\",\n" +
      "        \"createdBy\": \"PK\",\n" +
      "        \"updatedTimestamp\": null,\n" +
      "        \"updatedBy\": null\n" +
      "    }\n" +
      "]";
public static final String GET_PIPELINE_RESPONSE = "{\n" +
"    \"pipelineName\": \"Test_Pipeline\",\n" +
"    \"pipelineDescription\": \"test Pipeline\",\n" +
"    \"processMode\": \"Batch\",\n" +
"    \"tags\": \"ETL, Chimera\",\n" +
"    \"orgHierName\": \"Chimera\",\n" +
"    \"activeFlag\": \"N\",\n" +
"    \"createdTimestamp\": \"2025-01-06T13:49:41.220+00:00\",\n" +
"    \"createdBy\": \"PK\",\n" +
"    \"updatedTimestamp\": \"2025-01-08T12:01:36.216+00:00\",\n" +
"    \"updatedBy\": \"PK\"\n" +
"}";
    public static final String CREATE_PIPELINE_REQUEST = "{\n" +
      "    \"pipelineName\": \"Test_Pipeline\",\n" +
      "    \"pipelineDescription\": \"test Pipeline\",\n" +
      "    \"processMode\": \"Batch\",\n" +
      "    \"tags\": \"ETL, Chimera\",\n" +
      "    \"orgHierName\": \"Chimera\",\n" +
      "    \"activeFlag\": \"N\",\n" +
      "    \"createdTimestamp\": \"2025-01-06T13:49:41.220+00:00\",\n" +
      "    \"createdBy\": \"PK\",\n" +
      "    \"updatedTimestamp\": \"2025-01-08T12:01:36.216+00:00\",\n" +
      "    \"updatedBy\": \"PK\"\n" +
      "}";
      public static final String CREATE_PIPELINE_RESPONSE = "{\n" +
      "    \"message\": \"Transform Config created successfully for pipeline: Test_Pipeline with sequence: 1\",\n" +
      "    \"statusCode\": \"CREATED\"\n" +
      "}";
      public static final String UPDATE_PIPELINE_REQUEST = "{\n" +
      "    \"pipelineName\": \"Test_Pipeline\",\n" +
      "    \"tags\": \"ETL\",\n" +
      "    \"updatedBy\": \"PK\"\n" +
      "}";
      public static final String UPDATE_PIPELINE_RESPONSE = "{\n" +
      "    \"message\": \"Transform Config created successfully for pipeline: Test_Pipeline with sequence: 1\",\n" +
      "    \"statusCode\": \"CREATED\"\n" +
      "}";
}


