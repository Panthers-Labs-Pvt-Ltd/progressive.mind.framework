package com.progressive.minds.chimera.DataManagement.datalineage.facets;

import com.progressive.minds.chimera.DataManagement.datalineage.models.DataSourcesTypes;
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

    public OpenLineage.Job JobEndFacet(OpenLineage openLineageProducer,String name,String Namespace) {
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


    public static OpenLineage.JobFacets getJobFacet(OpenLineage openLineageProducer,
                                                 Map<String, String> JobInformation
                                                ) {
        String processingType = JobInformation.getOrDefault("ProcessingType", "Batch");
        String jobType = JobInformation.getOrDefault("JobType", "Ingestion");
        String integrationType = JobInformation.getOrDefault("IntegrationType", "spark");
        String owningDomain = JobInformation.getOrDefault("Domain", "-");
        String processingEngine = JobInformation.getOrDefault("ProcessingEngine", "spark");
        String DataSourceType = JobInformation.getOrDefault("DataSourceType", "Unknown");
        String DataSourceSubType = JobInformation.getOrDefault("DataSourceSubType", "Unknown");
        String JobDocumentation = JobInformation.getOrDefault("JobDocumentation", "NA");
        String Key = JobInformation.getOrDefault("Key", "NA");
        String Value = JobInformation.getOrDefault("Value", "NA");


//        String FileName = JobInformation.getOrDefault("FileName", "Unknown");
//        String Delimiter = JobInformation.getOrDefault("Delimiter", ",");
//        String Qualifier = JobInformation.getOrDefault("Qualifier", "\"");
//        String Size = JobInformation.getOrDefault("Size", "Unknown");
//        String Compression = JobInformation.getOrDefault("Compression", "Unknown");
//        String SQLQuery = JobInformation.getOrDefault("SQLQuery", "NA");
//        String SourceCodeLanguage = JobInformation.getOrDefault("SourceCodeLanguage", "Java");
//        String SourceCode = JobInformation.getOrDefault("SourceCode", "NA");

        String Branch= JobInformation.getOrDefault("Branch", "main");
        String Type= JobInformation.getOrDefault("Type", "Gitlab");
        String Version= JobInformation.getOrDefault("Version", "1.0");
        String RepositoryURL= JobInformation.getOrDefault("RepositoryURL", "www.gitlab.com");
        String Tag= JobInformation.getOrDefault("Tag", "release/1.0");
        String Path= JobInformation.getOrDefault("Path", "NA");

        // Job Definition Initialization
        OpenLineage.JobFacetsBuilder  jobFacets = openLineageProducer.newJobFacetsBuilder();
        jobFacets.jobType(JobTypeFacets(openLineageProducer, processingType, jobType,processingEngine, Key, Value));

        if (DataSourceType.equalsIgnoreCase(String.valueOf(DataSourcesTypes.FILE))) {
            jobFacets.put("File", openLineageProducer.newJobFacet());

        /*    CustomFacet.FileJobFacet fileFacet = new CustomFacet.FileJobFacet(BaseURI,
                    FileName ,DataSourceType,DataSourceSubType, Delimiter,Qualifier,Size,Compression);
            jobFacets.put("File", fileFacet);*/
        }

        else if (DataSourceType.equalsIgnoreCase(String.valueOf(DataSourcesTypes.RDBMS))) {
            OpenLineage.SQLJobFacet SQLFacet = openLineageProducer.newSQLJobFacetBuilder()
                    //.query(SQLQuery)
                    .build();
            jobFacets.sql(SQLFacet);
        }

        //TODO Define Custom Facet similar to FileJobFacet and use
        else if (DataSourceType.equalsIgnoreCase(String.valueOf(DataSourcesTypes.NOSQL))) {
            jobFacets.put("NOSQL", openLineageProducer.newJobFacet());
        }
        //TODO Define Custom Facet similar to FileJobFacet and use
        else if (DataSourceType.equalsIgnoreCase(String.valueOf(DataSourcesTypes.API))) {
            jobFacets.put("API", openLineageProducer.newJobFacet());

        }
        //TODO Define Custom Facet similar to FileJobFacet and use
        else if (DataSourceType.equalsIgnoreCase(String.valueOf(DataSourcesTypes.OpenTableFormat))) {
            jobFacets.put("OTF", openLineageProducer.newJobFacet());

        }
        //jobFacets.sourceCode(SourceCodeJobFacet(openLineageProducer, SourceCodeLanguage, SourceCode));
        jobFacets.documentation(DocumentationJobFacet(openLineageProducer, JobDocumentation));
        jobFacets.ownership(OwnershipJobFacet(openLineageProducer, JobInformation)); //TODO Put Correct Job Owner
        jobFacets.sourceCodeLocation(SourceCodeLocationJobFacet(openLineageProducer,
                Branch, Type, Version, RepositoryURL,Tag, Path));
        
        return jobFacets.build();
    }
}
