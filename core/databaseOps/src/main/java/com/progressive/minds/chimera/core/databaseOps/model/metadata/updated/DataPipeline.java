package com.progressive.minds.chimera.core.databaseOps.model.metadata.updated;

import java.util.UUID;
import java.sql.Timestamp;

public class DataPipeline {

    private UUID pipelineId;
    private String pipelineName;
    private String pipelineDescription;
    private String processMode;
    private String runFrequency;
    private UUID ownerOrgId;
    private Timestamp createdTimestamp;
    private String createdBy;
    private Timestamp updatedTimestamp;
    private String updatedBy;
    private String activeFlag;

    // Getters and Setters

    public UUID getPipelineId() {
        return pipelineId;
    }

    public void setPipelineId(UUID pipelineId) {
        this.pipelineId = pipelineId;
    }

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

    public UUID getOwnerOrgId() {
        return ownerOrgId;
    }

    public void setOwnerOrgId(UUID ownerOrgId) {
        this.ownerOrgId = ownerOrgId;
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
        return "DataPipeline{" +
                "pipelineId=" + pipelineId +
                ", pipelineName='" + pipelineName + '\'' +
                ", pipelineDescription='" + pipelineDescription + '\'' +
                ", processMode='" + processMode + '\'' +
                ", runFrequency='" + runFrequency + '\'' +
                ", ownerOrgId=" + ownerOrgId +
                ", createdTimestamp=" + createdTimestamp +
                ", createdBy='" + createdBy + '\'' +
                ", updatedTimestamp=" + updatedTimestamp +
                ", updatedBy='" + updatedBy + '\'' +
                ", activeFlag='" + activeFlag + '\'' +
                '}';
    }
}
