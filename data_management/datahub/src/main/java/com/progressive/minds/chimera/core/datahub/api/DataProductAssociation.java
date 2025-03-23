package com.progressive.minds.chimera.core.datahub.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.linkedin.common.AuditStamp;

import java.util.Map;

public class DataProductAssociation {
    @JsonProperty("outputPort")
    private boolean outputPort;

    @JsonProperty("sourceUrn")
    private String sourceUrn;

    @JsonProperty("destinationUrn")
    private String destinationUrn;

    @JsonProperty("created")
    private AuditStamp created;

    @JsonProperty("lastModified")
    private AuditStamp lastModified;

    @JsonProperty("properties")
    private Map<String, String> properties;

    // Getters and Setters
    public boolean isOutputPort() {
        return outputPort;
    }

    public void setOutputPort(boolean outputPort) {
        this.outputPort = outputPort;
    }

    public String getSourceUrn() {
        return sourceUrn;
    }

    public void setSourceUrn(String sourceUrn) {
        this.sourceUrn = sourceUrn;
    }

    public String getDestinationUrn() {
        return destinationUrn;
    }

    public void setDestinationUrn(String destinationUrn) {
        this.destinationUrn = destinationUrn;
    }

    public AuditStamp getCreated() {
        return created;
    }

    public void setCreated(AuditStamp created) {
        this.created = created;
    }

    public AuditStamp getLastModified() {
        return lastModified;
    }

    public void setLastModified(AuditStamp lastModified) {
        this.lastModified = lastModified;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}

