package com.progressive.minds.chimera.core.databaseOps.model.datahub;

import com.progressive.minds.chimera.core.databaseOps.annotation.Column;
import com.progressive.minds.chimera.core.databaseOps.annotation.Id;
import com.progressive.minds.chimera.core.databaseOps.annotation.Table;

import java.sql.Timestamp;

@Table(name = "metadata_aspect_v2")
public class MetadataAspectV2 {

    @Id
    @Column(name = "urn")
    private String urn;

    @Column(name = "aspect")
    private String aspect;

    @Column(name = "version")
    private long version;

    @Column(name = "metadata")
    private String metadata;

    @Column(name = "systemMetadata")
    private String systemMetadata;

    @Column(name = "createdOn")
    private Timestamp createdOn;

    @Column(name = "createdBy")
    private String createdBy;

    @Column(name = "createdFor")
    private String createdFor;

    // Getter and Setter for urn
    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    // Getter and Setter for aspect
    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    // Getter and Setter for version
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    // Getter and Setter for metadata
    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    // Getter and Setter for systemMetadata
    public String getSystemMetadata() {
        return systemMetadata;
    }

    public void setSystemMetadata(String systemMetadata) {
        this.systemMetadata = systemMetadata;
    }

    // Getter and Setter for createdOn
    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    // Getter and Setter for createdBy
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    // Getter and Setter for createdFor
    public String getCreatedFor() {
        return createdFor;
    }

    public void setCreatedFor(String createdFor) {
        this.createdFor = createdFor;
    }
}
