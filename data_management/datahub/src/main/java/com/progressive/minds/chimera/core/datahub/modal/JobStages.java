package com.progressive.minds.chimera.core.datahub.modal;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class JobStages {

    @NotNull
    private String stageName;

    @NotNull
    private String stageType;

    private String stageDescription;
    private String stageUrl;
    private List<Property> properties;
    private List<Dataset> inputDataset;
    private List<Dataset> outputDataset;
    private String stageStatus;
    private String domain;
    private List<Tag> tags;
    private List<Owners> owners;
    private List<GlossaryTerm> glossaryTerm;

    public JobStages(){}

    public JobStages(List<Owners> owners, String stageName, String stageType, String stageDescription, String stageUrl,
                     List<Property> properties, List<Dataset> inputDataset, List<Dataset> outputDataset,
                     String stageStatus, String domain, List<Tag> tags, List<GlossaryTerm> glossaryTerm) {
        this.owners = owners;
        this.stageName = stageName;
        this.stageType = stageType;
        this.stageDescription = stageDescription;
        this.stageUrl = stageUrl;
        this.properties = properties;
        this.inputDataset = inputDataset;
        this.outputDataset = outputDataset;
        this.stageStatus = stageStatus;
        this.domain = domain;
        this.tags = tags;
        this.glossaryTerm = glossaryTerm;
    }
}
