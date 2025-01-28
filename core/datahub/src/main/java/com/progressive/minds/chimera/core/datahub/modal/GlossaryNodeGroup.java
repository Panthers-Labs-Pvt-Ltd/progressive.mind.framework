package com.progressive.minds.chimera.core.datahub.modal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class GlossaryNodeGroup {
    @NotNull
    public String name;

    @NotNull
    public String definition;

    @Null
    public GlossaryNodeGroup parentGlossaryNodeGroup;

    @Null
    public List<Owners> Ownership;

    @Null
    public Map<String, String> customProperties;

    public GlossaryNodeGroup(){}

    // Constructor for GlossaryNodeGroup
    public GlossaryNodeGroup(String name, String definition, GlossaryNodeGroup parentGlossaryNodeGroup,
                             List<Owners> Ownership,
                             Map<String, String> customProperties) {
        this.name = name;
        this.definition = definition;
        this.parentGlossaryNodeGroup = parentGlossaryNodeGroup;
        this.Ownership = Ownership;
        this.customProperties = customProperties;
    }
}
