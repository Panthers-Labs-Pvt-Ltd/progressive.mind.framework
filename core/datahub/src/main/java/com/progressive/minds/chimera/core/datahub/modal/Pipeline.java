package com.progressive.minds.chimera.core.datahub.modal;


import javax.validation.constraints.NotNull;
import com.progressive.minds.chimera.core.datahub.modal.jobStages;
import com.progressive.minds.chimera.core.datahub.modal.Tag;
import com.progressive.minds.chimera.core.datahub.modal.Property;
import com.progressive.minds.chimera.core.datahub.modal.Owners;
import com.progressive.minds.chimera.core.datahub.modal.GlossaryTerm;

import java.util.List;

public class Pipeline {
    @NotNull
    public String pipelineName;
    @NotNull
    public String dataProductName;
    @NotNull
    public String processingEngine;
    public String pipelineDescription;
    @NotNull
    public String FabricType;
    public String uri;
    public String domainName;
    public boolean inActiveFlag;
    public List<Tag> tags;
    public List<Property> properties;
    public List<Owners> owners;
    public List<GlossaryTerm> glossaryTerm;
    @NotNull
    public List<jobStages> stages;
}
