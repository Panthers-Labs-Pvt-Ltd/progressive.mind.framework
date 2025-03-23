package com.progressive.minds.chimera.core.datahub.modal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Pipeline {

    public Pipeline(){}

    public Pipeline(List<Owners> owners, String pipelineName, String dataProductName, String processingEngine,
                    String pipelineDescription, String fabricType, String uri, String domainName, boolean inActiveFlag,
                    List<Tag> tags, List<Property> properties, List<GlossaryTerm> glossaryTerm, List<JobStages> stages) {
        this.owners = owners;
        this.pipelineName = pipelineName;
        this.dataProductName = dataProductName;
        this.processingEngine = processingEngine;
        this.pipelineDescription = pipelineDescription;
        this.fabricType = fabricType;
        this.uri = uri;
        this.domainName = domainName;
        this.inActiveFlag = inActiveFlag;
        this.tags = tags;
        this.properties = properties;
        this.glossaryTerm = glossaryTerm;
        this.stages = stages;
    }

    @NotNull
    @JsonProperty("pipelineName")
    private String pipelineName;
    @NotNull
    @JsonProperty("dataProductName")
    private String dataProductName;
    @NotNull
    @JsonProperty("processingEngine")
    private String processingEngine;
    @JsonProperty("pipelineDescription")
    private String pipelineDescription;
    @NotNull
    @JsonProperty("fabricType")
    private String fabricType;
    @JsonProperty("uri")
    private String uri;
    @JsonProperty("domainName")
    private String domainName;
    @JsonProperty("inActiveFlag")
    private boolean inActiveFlag;
    @JsonProperty("tags")
    private List<Tag> tags;
    @JsonProperty("properties")
    private List<Property> properties;
    @JsonProperty("owners")
    private List<Owners> owners;
    @JsonProperty("glossaryTerm")
    private List<GlossaryTerm> glossaryTerm;
    @JsonProperty("stages")
    @NotNull
    private List<JobStages> stages;
}
