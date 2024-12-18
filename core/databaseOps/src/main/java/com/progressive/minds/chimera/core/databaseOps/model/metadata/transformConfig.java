package com.progressive.minds.chimera.core.databaseOps.model.metadata;

import java.sql.Timestamp;


/**
 * Represents a Transform Configuration entity.
 */
public class transformConfig {

    private String pipelineName;
    private Integer sequenceNumber;
    private String sqlText;
    private String transformDataframeName;
    private Timestamp createdTimestamp;
    private String createdBy;
    private Timestamp updatedTimestamp;
    private String updatedBy;
    private String activeFlag;

    public transformConfig() {
    }

    public transformConfig(String pipelineName, Integer sequenceNumber, String sqlText,
                           String transformDataframeName, Timestamp createdTimestamp, String createdBy,
                           Timestamp updatedTimestamp, String updatedBy, String activeFlag) {
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
        return "TransformConfig{" +
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

