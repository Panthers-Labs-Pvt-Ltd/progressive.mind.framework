package com.progressive.minds.chimera.core.datahub.modal;


import javax.validation.constraints.NotNull;
import java.util.List;

public class Dataset {
    @NotNull
    public String dataProductName;
    @NotNull
    public String name;
    public String displayName;
    public String description;
    @NotNull
    public String FabricType;
    @NotNull
    public String datasetPlatformName;
    public String qualifiedName;
    public String uri;
    public String domain;
    public List<Tag> tags;
    public List<Property> properties;
    public List<Owners> owners;
    public List<GlossaryTerm> glossaryTerm;
    public List<Field> fields;
}
