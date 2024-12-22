package com.progressive.minds.chimera.core.databaseOps.model.metadata.updated;

import java.util.UUID;

public class TransformMetadataConfig {

    private UUID transformId;
    private Integer sequenceNumber;
    private UUID pipelineId;
    private String sqlText;
    private String transformDataFrameName;
    private java.sql.Timestamp createdTimestamp;
    private String createdBy;
    private java.sql.Timestamp updatedTimestamp;
    private String updatedBy;
    private String activeFlag;

    // Getters and Setters

    public UUID getTransformId() {
        return transformId;
    }

    public void setTransformId(UUID transformId) {
        this.transformId = transformId;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public UUID getPipelineId() {
        return pipelineId;
    }

    public void setPipelineId(UUID pipelineId) {
        this.pipelineId = pipelineId;
    }

    public String getSqlText() {
        return sqlText;
    }

    public void setSqlText(String sqlText) {
        this.sqlText = sqlText;
    }

    public String getTransformDataFrameName() {
        return transformDataFrameName;
    }

    public void setTransformDataFrameName(String transformDataFrameName) {
        this.transformDataFrameName = transformDataFrameName;
    }

    public java.sql.Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(java.sql.Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public java.sql.Timestamp getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(java.sql.Timestamp updatedTimestamp) {
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
        return "TransformConfig{" +
                "transformId=" + transformId +
                ", sequenceNumber=" + sequenceNumber +
                ", pipelineId=" + pipelineId +
                ", sqlText='" + sqlText + '\'' +
                ", transformDataFrameName='" + transformDataFrameName + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                ", createdBy='" + createdBy + '\'' +
                ", updatedTimestamp=" + updatedTimestamp +
                ", updatedBy='" + updatedBy + '\'' +
                ", activeFlag='" + activeFlag + '\'' +
                '}';
    }
}
