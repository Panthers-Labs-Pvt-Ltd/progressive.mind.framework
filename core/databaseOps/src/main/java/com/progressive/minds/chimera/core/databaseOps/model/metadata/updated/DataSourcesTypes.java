package com.progressive.minds.chimera.core.databaseOps.model.metadata.updated;

import java.util.Date;
import java.util.UUID;

public class DataSourcesTypes {

    private UUID dataSourceId;
    private String dataSourceType;
    private String dataSourceSubType;
    private String description;
    private String dataSourceTemplate;
    private String readConfig;
    private String writeConfig;
    private Date createdTimestamp;
    private String createdBy;
    private Date updatedTimestamp;
    private String updatedBy;
    private String activeFlag;

    // Getters and Setters

    public UUID getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(UUID dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(String dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public String getDataSourceSubType() {
        return dataSourceSubType;
    }

    public void setDataSourceSubType(String dataSourceSubType) {
        this.dataSourceSubType = dataSourceSubType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDataSourceTemplate() {
        return dataSourceTemplate;
    }

    public void setDataSourceTemplate(String dataSourceTemplate) {
        this.dataSourceTemplate = dataSourceTemplate;
    }

    public String getReadConfig() {
        return readConfig;
    }

    public void setReadConfig(String readConfig) {
        this.readConfig = readConfig;
    }

    public String getWriteConfig() {
        return writeConfig;
    }

    public void setWriteConfig(String writeConfig) {
        this.writeConfig = writeConfig;
    }

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Date createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(Date updatedTimestamp) {
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
        return "DATA_SOURCES_TYPES{" +
                "dataSourceId=" + dataSourceId +
                ", dataSourceType='" + dataSourceType + '\'' +
                ", dataSourceSubType='" + dataSourceSubType + '\'' +
                ", description='" + description + '\'' +
                ", dataSourceTemplate='" + dataSourceTemplate + '\'' +
                ", readConfig='" + readConfig + '\'' +
                ", writeConfig='" + writeConfig + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                ", createdBy='" + createdBy + '\'' +
                ", updatedTimestamp=" + updatedTimestamp +
                ", updatedBy='" + updatedBy + '\'' +
                ", activeFlag='" + activeFlag + '\'' +
                '}';
    }
}
