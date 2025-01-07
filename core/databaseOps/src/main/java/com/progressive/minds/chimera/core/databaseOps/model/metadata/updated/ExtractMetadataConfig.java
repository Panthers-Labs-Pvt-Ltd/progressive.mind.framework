package com.progressive.minds.chimera.core.databaseOps.model.metadata.updated;

import java.util.UUID;
import java.sql.Timestamp;

public class ExtractMetadataConfig {

    private UUID extractId;
    private int sequenceNumber;
    private UUID pipelineId;
    private String dataframeName;
    private String sourceConfiguration;
    private String predecessorSequences;
    private String successorSequences;
    private UUID dataSourceConnectionId;
    private Timestamp createdTimestamp;
    private String createdBy;
    private Timestamp updatedTimestamp;
    private String updatedBy;
    private String activeFlag;

    // Getters and Setters

    public UUID getExtractId() {
        return extractId;
    }

    public void setExtractId(UUID extractId) {
        this.extractId = extractId;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public UUID getPipelineId() {
        return pipelineId;
    }

    public void setPipelineId(UUID pipelineId) {
        this.pipelineId = pipelineId;
    }

    public String getDataframeName() {
        return dataframeName;
    }

    public void setDataframeName(String dataframeName) {
        this.dataframeName = dataframeName;
    }

    public String getSourceConfiguration() {
        return sourceConfiguration;
    }

    public void setSourceConfiguration(String sourceConfiguration) {
        this.sourceConfiguration = sourceConfiguration;
    }

    public String getPredecessorSequences() {
        return predecessorSequences;
    }

    public void setPredecessorSequences(String predecessorSequences) {
        this.predecessorSequences = predecessorSequences;
    }

    public String getSuccessorSequences() {
        return successorSequences;
    }

    public void setSuccessorSequences(String successorSequences) {
        this.successorSequences = successorSequences;
    }

    public UUID getDataSourceConnectionId() {
        return dataSourceConnectionId;
    }

    public void setDataSourceConnectionId(UUID dataSourceConnectionId) {
        this.dataSourceConnectionId = dataSourceConnectionId;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(Timestamp updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }

    @Override
    public String toString() {
        return "ExtractMetadataConfig{" +
                "extractId=" + extractId +
                ", sequenceNumber=" + sequenceNumber +
                ", pipelineId=" + pipelineId +
                ", dataframeName='" + dataframeName + '\'' +
                ", sourceConfiguration='" + sourceConfiguration + '\'' +
                ", predecessorSequences='" + predecessorSequences + '\'' +
                ", successorSequences='" + successorSequences + '\'' +
                ", dataSourceConnectionId=" + dataSourceConnectionId +
                ", createdTimestamp=" + createdTimestamp +
                ", createdBy='" + createdBy + '\'' +
                ", updatedTimestamp=" + updatedTimestamp +
                ", updatedBy='" + updatedBy + '\'' +
                ", activeFlag='" + activeFlag + '\'' +
                '}';
    }
}
