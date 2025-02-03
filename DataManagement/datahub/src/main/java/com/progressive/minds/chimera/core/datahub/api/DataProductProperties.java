package com.progressive.minds.chimera.core.datahub.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.linkedin.common.ExternalReference;
import com.linkedin.dataproduct.DataProductAssociation;

import java.util.List;
import java.util.Map;

public class DataProductProperties {
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("assets")
    private List<DataProductAssociation> assets;

    @JsonProperty("customProperties")
    private Map<String, String> customProperties;

    @JsonProperty("externalReferences")
    private List<ExternalReference> externalReferences;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DataProductAssociation> getAssets() {
        return assets;
    }

    public void setAssets(List<DataProductAssociation> assets) {
        this.assets = assets;
    }

    public Map<String, String> getCustomProperties() {
        return customProperties;
    }

    public void setCustomProperties(Map<String, String> customProperties) {
        this.customProperties = customProperties;
    }

    public List<ExternalReference> getExternalReferences() {
        return externalReferences;
    }

    public void setExternalReferences(List<ExternalReference> externalReferences) {
        this.externalReferences = externalReferences;
    }
}
