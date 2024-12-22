package com.progressive.minds.chimera.core.databaseOps.model.metadata.updated;

import java.util.UUID;

public class RelationalExtractMetadataConfig extends ExtractMetadataConfig {

    private String tableName;
    private String schemaName;
    private String sqlText;
    private String activeFlag;

    // Getters and Setters

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getSqlText() {
        return sqlText;
    }

    public void setSqlText(String sqlText) {
        this.sqlText = sqlText;
    }

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }

    @Override
    public String toString() {
        return "RelationalExtractMetadataConfig{" +
                "tableName='" + tableName + '\'' +
                ", schemaName='" + schemaName + '\'' +
                ", sqlText='" + sqlText + '\'' +
                ", activeFlag='" + activeFlag + '\'' +
                "} " + super.toString();
    }
}
