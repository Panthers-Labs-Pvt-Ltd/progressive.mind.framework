package com.progressive.minds.chimera.DataManagement.openLineage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.progressive.minds.chimera.consumer.DBAPIClient;
import com.progressive.minds.chimera.dto.ExtractMetadata;
import com.progressive.minds.chimera.dto.PersistMetadata;
import com.progressive.minds.chimera.dto.PipelineMetadata;
import com.progressive.minds.chimera.dto.TransformMetadataConfig;
import io.openlineage.client.OpenLineage;
import io.openlineage.spark.agent.facets.EnvironmentFacet;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.apache.spark.sql.SaveMode.Overwrite;
import static org.junit.jupiter.api.Assertions.*;

class LineageTest {

    SparkSession spark = SparkSession.builder()
            .appName("LineageTest")
            .master("local[*]")
      //    .config("spark.extraListeners", "io.openlineage.spark.agent.OpenLineageSparkListener")  // Enable OpenLineage Listener
        //  .config("spark.openlineage.transport.type", "http")  // Transport type (HTTP)
     //       .config("spark.openlineage.transport.url", "http://localhost:1337/")  // Transport type (HTTP)
//           .config("spark.openlineage.transport.location", "/mnt/f/lineage.json")  // Marquez API URL
      //      .config("spark.openlineage.namespace", "LineageTestJobNamespace")  // OpenLineage Namespace
     //       .config("spark.openlineage.parentJobName", "LineageTestJob")  // Parent Job Name*/
            .getOrCreate();


    @Test
    void getLineage() throws Exception {
            java. util. Map<String, Object> environmentDetails= new HashMap<>();

            EnvironmentFacet EV = new EnvironmentFacet(environmentDetails);

            OpenLineage.RunEvent.EventType eventType = OpenLineage.RunEvent.EventType.START;
           // System.setProperty("API_CLIENT", "chimera_api_client");
           // System.setProperty("API_SECRET", "yhKj2HkNBpyv9ZgV9oqPxHcZOPEb3uBg");
            DBAPIClient dbClient = new DBAPIClient();
            PipelineMetadata inPipelineMetadata = dbClient.get("http://localhost:8888/api/v1/pipelineMetadata/edl.customer.info",
                    new TypeReference<PipelineMetadata>() {
                    });

            List<ExtractMetadata> extracts = inPipelineMetadata.getExtractMetadata();
            List<TransformMetadataConfig> transforms = inPipelineMetadata.getTransformMetadata();
            List<PersistMetadata> persists = inPipelineMetadata.getPersistMetadata();
            Map<String, Dataset<Row>> dataFrameMap = new HashMap<>();

            extracts.forEach(extract ->
            {
                Dataset<Row> datasetRow = spark.read()
                        .format(extract.getExtractSourceSubType())
                        .option("header", "true")
                        .option("inferSchema", "true")
                        .load(extract.getFileMetadata().getFilePath());
                datasetRow.printSchema();
                String DataframeName = extract.getDataframeName();
                dataFrameMap.put(DataframeName, datasetRow);
                datasetRow.createOrReplaceTempView(DataframeName);
            });

            transforms.forEach(transform ->
            {
                Dataset<Row> datasetRow =spark.sql(transform.getSqlText());
                dataFrameMap.put(transform.getTransformDataframeName(), datasetRow);
                datasetRow.createOrReplaceTempView(transform.getTransformDataframeName());
            });

            persists.forEach(persist ->{
                String jdbcUrl = "jdbc:postgresql://localhost:5432/chimera_db";
                Properties connectionProperties = new Properties();
                connectionProperties.put("user", "chimera");
                connectionProperties.put("password", "chimera123");
                connectionProperties.put("driver", "org.postgresql.Driver");
                Dataset<Row> resultDF =spark.sql(persist.getTargetSql());
                resultDF.write()
                        .mode(Overwrite)
                        .jdbc(jdbcUrl, persist.getTableName(), connectionProperties);
            });
        Lineage lineage = new Lineage();
       String Lineage = lineage.getLineage(inPipelineMetadata, spark);
                System.out.print(Lineage);
            //   String Lineage = ChimeraOpenLineage.OpenLineageWrapper(eventType,inPipelineMetadata,  spark, "file",lineageMap);
        }
    }
