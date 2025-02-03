package com.progressive.minds.chimera.core.datahub.modal;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
public class Dataset {

    // Getters and Setters
    @NotNull
    private String dataProductName;

    @NotNull
    private String name;

    private String displayName;
    private String description;

    @NotNull
    private String fabricType; // Renamed to camelCase

    @NotNull
    private String datasetPlatformName;

    private String qualifiedName;
    private String uri;
    private String domain;

    private List<Tag> tags;
    private List<Property> properties;
    private List<Owners> owners;
    private List<GlossaryTerm> glossaryTerm;
    private List<Field> fields;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public @NotNull String getDataProductName() {
        return dataProductName;
    }

    public void setDataProductName(@NotNull String dataProductName) {
        this.dataProductName = dataProductName;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public @NotNull String getFabricType() {
        return fabricType;
    }

    public void setFabricType(@NotNull String fabricType) {
        this.fabricType = fabricType;
    }

    public @NotNull String getDatasetPlatformName() {
        return datasetPlatformName;
    }

    public void setDatasetPlatformName(@NotNull String datasetPlatformName) {
        this.datasetPlatformName = datasetPlatformName;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public List<Owners> getOwners() {
        return owners;
    }

    public void setOwners(List<Owners> owners) {
        this.owners = owners;
    }

    public List<GlossaryTerm> getGlossaryTerm() {
        return glossaryTerm;
    }

    public void setGlossaryTerm(List<GlossaryTerm> glossaryTerm) {
        this.glossaryTerm = glossaryTerm;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    // Default Constructor
    public Dataset(){}

    // Constructor
    public Dataset(String dataProductName, String name, String fabricType, String datasetPlatformName) {
        this.dataProductName = dataProductName;
        this.name = name;
        this.fabricType = fabricType;
        this.datasetPlatformName = datasetPlatformName;
    }

}
