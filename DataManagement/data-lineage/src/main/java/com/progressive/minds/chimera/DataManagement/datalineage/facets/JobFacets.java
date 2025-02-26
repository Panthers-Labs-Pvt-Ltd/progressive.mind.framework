package com.progressive.minds.chimera.DataManagement.datalineage.facets;

import com.progressive.minds.chimera.DataManagement.datalineage.datasources.DataSourcesTypes;
import io.openlineage.client.OpenLineage;

import javax.annotation.Nullable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JobFacets {

    static URI BaseURI = URI.create("https://openlineage.io/spec/2-0-2/OpenLineage.json#/$defs/JobFacet");

    public static OpenLineage.Job JobStartFacet(OpenLineage openLineageProducer, String Namespace,
                                                String name, OpenLineage.JobFacets jobFacets) {
        return openLineageProducer.newJobBuilder()
                .namespace(Namespace)
                .name(name)
                .facets(jobFacets)
                .build();
    }

    public static OpenLineage.Job JobEndFacet(OpenLineage openLineageProducer, String name, String Namespace) {
        return openLineageProducer.newJobBuilder()
                .namespace(Namespace)
                .name(name)
                .build();
    }
    public static OpenLineage.JobTypeJobFacet JobTypeFacets(OpenLineage openLineageProducer, String processingType, String jobType,
                                                            String processingEngine, String Key, String Value) {

        return openLineageProducer.newJobTypeJobFacetBuilder()
                .processingType(processingType)
                .jobType(jobType)
                .integration(processingEngine)
                .put(Key,Value)
                .build();

    }

    public static OpenLineage.DocumentationJobFacet DocumentationJobFacet(OpenLineage openLineageProducer,
                                                                          String description) {
        return  openLineageProducer.newDocumentationJobFacetBuilder()
                .description(description).build();
    }

    public static OpenLineage.SourceCodeJobFacet SourceCodeJobFacet(OpenLineage openLineageProducer,
                                                                          String CodeLanguage, String CodeSnippets) {
        return  openLineageProducer.newSourceCodeJobFacet(CodeLanguage, CodeSnippets);

    }

    public static OpenLineage.SourceCodeLocationJobFacet SourceCodeLocationJobFacet(OpenLineage openLineageProducer,
                                                                    String Branch, String Type, String Version,
                                                                                    String RepositoryURL, String Tag,
                                                                                    String Path) {
        return
                openLineageProducer.newSourceCodeLocationJobFacetBuilder().branch(Branch)
                        .type(Type).version(Version).repoUrl(RepositoryURL).tag(Tag).path(Path).build();

    }

    public static OpenLineage.OwnershipJobFacet OwnershipJobFacet(OpenLineage openLineageProducer,
                                                                  @Nullable Map<String, String> ownerInfo)  {

        // Extract owner fields from the map
        assert ownerInfo != null;
        String name = ownerInfo.getOrDefault("name", "Unknown");
        String type = ownerInfo.getOrDefault("type", "Unknown");
        List<OpenLineage.OwnershipJobFacetOwners> owners = new ArrayList<>();
        owners.add(openLineageProducer.newOwnershipJobFacetOwners(name, type));
        return openLineageProducer.newOwnershipJobFacetBuilder().owners(owners).build();
    }
/*

        JobMap.put("processingType" , Optional.ofNullable(inPipelineMetadata.getProcessMode()).orElse("Batch"));
        JobMap.put("jobType" , "ETL");
        JobMap.put("pipelineName" , inPipelineMetadata.getPipelineName());
        JobMap.put("domain" , inPipelineMetadata.getOrgHierName());
        JobMap.put("integrationType" , "Spark");
        JobMap.put("jobDocumentation" , inPipelineMetadata.getPipelineDescription());
        JobMap.put("processingMode", inPipelineMetadata.getProcessMode());
        JobMap.put("owningDomain", inPipelineMetadata.getOrgHierName());
        JobMap.put("executionEngine", "spark");
        JobMap.put("appName", inSparkSession.sparkContext().appName());
        JobMap.put("applicationId", inSparkSession.sparkContext().applicationId());
        JobMap.put("deployMode", inSparkSession.sparkContext().deployMode());
        JobMap.put("driverHost", inSparkSession.conf().get("spark.driver.host", "Not Available"));
        JobMap.put("userName", System.getProperty("user.name"));

 */


    public static OpenLineage.JobFacets getJobFacet(OpenLineage openLineageProducer,String SQL,
                                                 Map<String, String> JobInformation
                                                ) {
        String processingType = JobInformation.getOrDefault("processingType", "Batch");
        String jobType = JobInformation.getOrDefault("jobType", "Ingestion");
        String integrationType = JobInformation.getOrDefault("integrationType", "spark");
        String owningDomain = JobInformation.getOrDefault("domain", "-");
        String processingEngine = JobInformation.getOrDefault("processingEngine", "spark");
        String DataSourceType = JobInformation.getOrDefault("dataSourceType", "Unknown");
        String JobDocumentation = JobInformation.getOrDefault("jobDocumentation", "NA");

        String Branch= JobInformation.getOrDefault("Branch", "main");
        String Type= JobInformation.getOrDefault("Type", "Gitlab");
        String Version= JobInformation.getOrDefault("Version", "1.0");
        String RepositoryURL= JobInformation.getOrDefault("RepositoryURL", "www.gitlab.com");
        String Tag= JobInformation.getOrDefault("Tag", "release/1.0");
        String Path= JobInformation.getOrDefault("Path", "NA");

        // Job Definition Initialization
        OpenLineage.JobFacetsBuilder  jobFacets = openLineageProducer.newJobFacetsBuilder();
        jobFacets.jobType(JobTypeFacets(openLineageProducer, processingType, jobType,processingEngine, "domain", owningDomain));

        if (DataSourceType.equalsIgnoreCase(String.valueOf(DataSourcesTypes.FILE))) {
            jobFacets.put("File", openLineageProducer.newJobFacet());

        /*    CustomFacet.FileJobFacet fileFacet = new CustomFacet.FileJobFacet(BaseURI,
                    FileName ,DataSourceType,DataSourceSubType, Delimiter,Qualifier,Size,Compression);
            jobFacets.put("File", fileFacet);*/
        }

        else if (DataSourceType.equalsIgnoreCase(String.valueOf(DataSourcesTypes.RDBMS))) {
            OpenLineage.SQLJobFacet SQLFacet = openLineageProducer.newSQLJobFacetBuilder()
                    ///.query(SQLQuery)
                    .build();
            jobFacets.sql(SQLFacet);
        }

        else if (DataSourceType.equalsIgnoreCase(String.valueOf(DataSourcesTypes.NOSQL))) {
            jobFacets.put("NOSQL", openLineageProducer.newJobFacet());
        }
        else if (DataSourceType.equalsIgnoreCase(String.valueOf(DataSourcesTypes.API))) {
            jobFacets.put("API", openLineageProducer.newJobFacet());

        }
        else if (DataSourceType.equalsIgnoreCase(String.valueOf(DataSourcesTypes.OpenTableFormat))) {
            jobFacets.put("OTF", openLineageProducer.newJobFacet());

        }
        //jobFacets.sourceCode(SourceCodeJobFacet(openLineageProducer, SourceCodeLanguage, SourceCode));
        if (SQL != null && !SQL.isEmpty())
            jobFacets.sql(openLineageProducer.newSQLJobFacet(SQL));
        else
            jobFacets.sql(openLineageProducer.newSQLJobFacet("Select * from " + DataSourceType));

        jobFacets.documentation(DocumentationJobFacet(openLineageProducer, JobDocumentation));

        jobFacets.ownership(OwnershipJobFacet(openLineageProducer, JobInformation)); //TODO Put Correct Job Owner
        jobFacets.sourceCodeLocation(SourceCodeLocationJobFacet(openLineageProducer,
                Branch, Type, Version, RepositoryURL,Tag, Path));
        
        return jobFacets.build();
    }
}
