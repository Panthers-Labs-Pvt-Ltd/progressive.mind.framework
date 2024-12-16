package com.progressive.minds.chimera.core.databaseOps.model;



import java.sql.Timestamp;


/**
 * Represents a Chimera Data Source entity.
 */
public class dataSources {

    private String dataSourceType;
    private String dataSourceSubType;
    private String description;
    private String readDefaults;
    private String writeDefaults;
    private Timestamp createdTimestamp;
    private String createdBy;
    private Timestamp updatedTimestamp;
    private String updatedBy;
    private String activeFlag;

    public dataSources(String dataSourceType, String dataSourceSubType, String description, String readDefaults,
                             String writeDefaults, Timestamp createdTimestamp, String createdBy,
                             Timestamp updatedTimestamp, String updatedBy, String activeFlag) {
        this.dataSourceType = dataSourceType;
        this.dataSourceSubType = dataSourceSubType;
        this.description = description;
        this.readDefaults = readDefaults;
        this.writeDefaults = writeDefaults;
        this.createdTimestamp = createdTimestamp;
        this.createdBy = createdBy;
        this.updatedTimestamp = updatedTimestamp;
        this.updatedBy = updatedBy;
        this.activeFlag = activeFlag;
    }

    public dataSources() {}


    // Getters and Setters

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

    public String getReadDefaults() {
        return readDefaults;
    }

    public void setReadDefaults(String readDefaults) {
        this.readDefaults = readDefaults;
    }

    public String getWriteDefaults() {
        return writeDefaults;
    }

    public void setWriteDefaults(String writeDefaults) {
        this.writeDefaults = writeDefaults;
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
        return "ChimeraDataSource{" +
                "dataSourceType='" + dataSourceType + '\'' +
                ", dataSourceSubType='" + dataSourceSubType + '\'' +
                ", description='" + description + '\'' +
                ", readDefaults='" + readDefaults + '\'' +
                ", writeDefaults='" + writeDefaults + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                ", createdBy='" + createdBy + '\'' +
                ", updatedTimestamp=" + updatedTimestamp +
                ", updatedBy='" + updatedBy + '\'' +
                ", activeFlag='" + activeFlag + '\'' +
                '}';
    }
}
