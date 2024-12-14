package com.progressive.minds.chimera.core.databaseOps.model;

import java.time.LocalDateTime;

/**
 * Represents a Transform Configuration entity.
 */
public class transformConfig {

    private int uniqueId;
    private String pipelineName;
    private Integer sequenceNumber;
    private String sqlText;
    private String transformDataframeName;
    private LocalDateTime createdTimestamp;
    private String createdBy;
    private LocalDateTime updatedTimestamp;
    private String updatedBy;
    private String activeFlag;

    public transformConfig() {
    }

    public transformConfig(int uniqueId, String pipelineName, Integer sequenceNumber, String sqlText,
                           String transformDataframeName, LocalDateTime createdTimestamp, String createdBy,
                           LocalDateTime updatedTimestamp, String updatedBy, String activeFlag) {
        this.uniqueId = uniqueId;
        this.pipelineName = pipelineName;
        this.sequenceNumber = sequenceNumber;
        this.sqlText = sqlText;
        this.transformDataframeName = transformDataframeName;
        this.createdTimestamp = createdTimestamp;
        this.createdBy = createdBy;
        this.updatedTimestamp = updatedTimestamp;
        this.updatedBy = updatedBy;
        this.activeFlag = activeFlag;
    }

    // Getters and Setters

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getPipelineName() {
        return pipelineName;
    }

    public void setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getSqlText() {
        return sqlText;
    }

    public void setSqlText(String sqlText) {
        this.sqlText = sqlText;
    }

    public String getTransformDataframeName() {
        return transformDataframeName;
    }

    public void setTransformDataframeName(String transformDataframeName) {
        this.transformDataframeName = transformDataframeName;
    }

    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(LocalDateTime updatedTimestamp) {
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
        return "TransformConfig{" +
                "uniqueId=" + uniqueId +
                ", pipelineName='" + pipelineName + '\'' +
                ", sequenceNumber=" + sequenceNumber +
                ", sqlText='" + sqlText + '\'' +
                ", transformDataframeName='" + transformDataframeName + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                ", createdBy='" + createdBy + '\'' +
                ", updatedTimestamp=" + updatedTimestamp +
                ", updatedBy='" + updatedBy + '\'' +
                ", activeFlag='" + activeFlag + '\'' +
                '}';
    }
}

