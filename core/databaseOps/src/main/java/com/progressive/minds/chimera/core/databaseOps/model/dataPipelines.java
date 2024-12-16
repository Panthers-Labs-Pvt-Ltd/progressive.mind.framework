package com.progressive.minds.chimera.core.databaseOps.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Represents a Chimera Pipeline entity.
 */
public class dataPipelines {

    private String pipelineName;
    private String pipelineDescription;
    private String processMode;
    private String runFrequency;
    private Timestamp createdTimestamp;
    private String createdBy;
    private Timestamp updatedTimestamp;
    private String updatedBy;
    private String activeFlag;

    public dataPipelines() {}

    public dataPipelines(String pipelineName, String pipelineDescription, String processMode,
                         String runFrequency, Timestamp createdTimestamp, String createdBy,
                         Timestamp updatedTimestamp, String updatedBy, String activeFlag) {
        this.pipelineName = pipelineName;
        this.pipelineDescription = pipelineDescription;
        this.processMode = processMode;
        this.runFrequency = runFrequency;
        this.createdTimestamp = createdTimestamp;
        this.createdBy = createdBy;
        this.updatedTimestamp = updatedTimestamp;
        this.updatedBy = updatedBy;
        this.activeFlag = activeFlag;
    }

    // Getters and Setters

    public String getPipelineName() {
        return pipelineName;
    }

    public void setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
    }

    public String getPipelineDescription() {
        return pipelineDescription;
    }

    public void setPipelineDescription(String pipelineDescription) {
        this.pipelineDescription = pipelineDescription;
    }

    public String getProcessMode() {
        return processMode;
    }

    public void setProcessMode(String processMode) {
        this.processMode = processMode;
    }

    public String getRunFrequency() {
        return runFrequency;
    }

    public void setRunFrequency(String runFrequency) {
        this.runFrequency = runFrequency;
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

    // toString Method

    @Override
    public String toString() {
        return "ChimeraPipeline{" +
                "pipelineName='" + pipelineName + '\'' +
                ", pipelineDescription='" + pipelineDescription + '\'' +
                ", processMode='" + processMode + '\'' +
                ", runFrequency='" + runFrequency + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                ", createdBy='" + createdBy + '\'' +
                ", updatedTimestamp=" + updatedTimestamp +
                ", updatedBy='" + updatedBy + '\'' +
                ", activeFlag='" + activeFlag + '\'' +
                '}';
    }
}
