package com.progressive.minds.chimera.core.datahub.modal;

import javax.validation.constraints.NotNull;
import java.util.List;


public class jobStages {

    @NotNull
    public String stageName;
    @NotNull
    public String stageType;
    public String stageDescription;
    public String stageUrl;
    public List<Property> properties;
    public List<Dataset> inputDataset;
    public List<Dataset> outputDataset;
    public String stageStatus;
    public String domain;
    public List<Tag> tags;
    public List<Owners> owners;
    public List<GlossaryTerm> glossaryTerm;
}

