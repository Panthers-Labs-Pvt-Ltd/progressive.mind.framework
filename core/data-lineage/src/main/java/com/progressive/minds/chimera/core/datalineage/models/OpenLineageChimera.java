package com.progressive.minds.chimera.core.datalineage.models;

import com.progressive.minds.chimera.core.databaseOps.model.metadata.*;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.*;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import io.openlineage.client.OpenLineage;
import io.openlineage.client.OpenLineage.RunEvent;
import io.openlineage.client.OpenLineage.OutputDataset;
import io.openlineage.client.OpenLineage.InputDataset;
import io.openlineage.client.utils.UUIDUtils;
import org.apache.spark.sql.SparkSession;

import java.net.URI;
import java.util.*;

import static com.progressive.minds.chimera.core.datalineage.facets.JobFacets.JobFacet;
import static com.progressive.minds.chimera.core.datalineage.facets.JobFacets.JobStartFacet;
import static com.progressive.minds.chimera.core.datalineage.utils.Utility.getTableNamesFromSQL;

public class OpenLineageChimera {
    static ChimeraLogger LineageLogger =  ChimeraLoggerFactory.getLogger(OpenLineageTransportTypes.class);
    static UUID runId;
    List<OutputDataset> outputDataset = new ArrayList<>();
    List<InputDataset> inputDataset = new ArrayList<>();
    public static RunEvent generateLineage(SparkSession inSparkSession, String inPipelineName)
    {
        Map<String, Object> pipelineFilter = Map.of("pipelineName", inPipelineName);

        extractViewRepository extractService = new extractViewRepository();
        transformConfigRepository transformService = new transformConfigRepository();
        persistViewRepository persistService = new persistViewRepository();

        List<extractView>   extractConfigList  = extractService.getExtractDetailsWithFilters(pipelineFilter);
        List<transformConfig> transformConfigList  = transformService.getTransformConfigWithFilters(pipelineFilter);
        List<persistView>   persistConfigList  = persistService.getPersistDetailsWithFilters(pipelineFilter);
        //  StringBuilder LineageEvent = new StringBuilder("[");

        String PRODUCER_NAME = "https://github.com/OpenLineage/OpenLineage/tree/1.25.0/integration/spark";
        URI producer = URI.create(PRODUCER_NAME);
        OpenLineage openLineageProducer = new OpenLineage(producer);


        if (runId == null){
            runId = UUIDUtils.generateNewUUID();
        }

        extractConfigList
                .forEach(extract -> {
                    String TableName = extract.getTableName();
                    List<String> extractTableList = getTableNamesFromSQL(inSparkSession, extract.getSqlText());
                    LineageLogger.logInfo("Processing Lineage for config: " + extract.getSequenceNumber());

                    // Job Documentation Type Initialization
                    Map<String, String>  extraInfo = new HashMap<>();
                    extraInfo.put("DataSourceType",extract.getDataSourceType());
                    extraInfo.put("DataSourceSubType",extract.getDataSourceSubType());
                    extraInfo.put("processingType",extract.getProcessMode());
                    extraInfo.put("jobType","Ingestion");
                    extraInfo.put("FileName",extract.getFileName());
                    extraInfo.put("Delimiter","TBD");
                    extraInfo.put("Qualifier","TBD");
                    extraInfo.put("Size","TBD");
                    extraInfo.put("Compression","TBD");
                    extraInfo.put("SQLQuery",extract.getSqlText());
                    extraInfo.put("Key","TBD");
                    extraInfo.put("Value","TBD");

                    extraInfo.put("SourceCodeLanguage","Java");
                    extraInfo.put("SourceCode","TBD");
                    extraInfo.put("JobDocumentation",extract.getPipelineDescription());

                    extraInfo.put("Branch","main");
                    extraInfo.put("Type","Gitlab");
                    extraInfo.put("Version","1.0");
                    extraInfo.put("RepositoryURL","www.gitlab.com");
                    extraInfo.put("Tag","release/1.0");
                    extraInfo.put("Path","NA");

                    //Job Information
                    JobStartFacet(openLineageProducer, "extract.getNamespace",  // TBD NameSpace Confirmation
                            extract.getPipelineName(),
                            JobFacet(openLineageProducer, extraInfo));

                // Inbound
                    //Transformation
                    //Outbound
                    //Run End

                });
        return null;
    }
}


