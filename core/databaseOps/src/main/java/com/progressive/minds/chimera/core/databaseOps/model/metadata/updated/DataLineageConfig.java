package com.progressive.minds.chimera.core.databaseOps.model.metadata.updated;

import java.util.UUID;

public class DataLineageConfig {

    private UUID lineageId;
    private String batchId;
    private String pipelineName;
    private String databaseName;
    private String tableName;
    private String lineageMetadata;
    private String reserved5;
    private String reserved4;
    private String reserved3;
    private String reserved2;
    private String reserved1;
    private java.sql.Timestamp createdTs;
    private String createdBy;
    private java.sql.Timestamp updatedTs;
    private String updatedBy;
    private String activeFlg;

    // Getters and Setters

    public UUID getLineageId() {
        return lineageId;
    }

    public void setLineageId(UUID lineageId) {
        this.lineageId = lineageId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getPipelineName() {
        return pipelineName;
    }

    public void setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getLineageMetadata() {
        return lineageMetadata;
    }

    public void setLineageMetadata(String lineageMetadata) {
        this.lineageMetadata = lineageMetadata;
    }

    public String getReserved5() {
        return reserved5;
    }

    public void setReserved5(String reserved5) {
        this.reserved5 = reserved5;
    }

    public String getReserved4() {
        return reserved4;
    }

    public void setReserved4(String reserved4) {
        this.reserved4 = reserved4;
    }

    public String getReserved3() {
        return reserved3;
    }

    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3;
    }

    public String getReserved2() {
        return reserved2;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }

    public String getReserved1() {
        return reserved1;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    public java.sql.Timestamp getCreatedTs() {
        return createdTs;
    }

    public void setCreatedTs(java.sql.Timestamp createdTs) {
        this.createdTs = createdTs;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public java.sql.Timestamp getUpdatedTs() {
        return updatedTs;
    }

    public void setUpdatedTs(java.sql.Timestamp updatedTs) {
        this.updatedTs = updatedTs;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getActiveFlg() {
        return activeFlg;
    }

    public void setActiveFlg(String activeFlg) {
        this.activeFlg = activeFlg;
    }

    @Override
    public String toString() {
        return "DataLineageConfig{" +
                "lineageId=" + lineageId +
                ", batchId='" + batchId + '\'' +
                ", pipelineName='" + pipelineName + '\'' +
                ", databaseName='" + databaseName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", lineageMetadata='" + lineageMetadata + '\'' +
                ", reserved5='" + reserved5 + '\'' +
                ", reserved4='" + reserved4 + '\'' +
                ", reserved3='" + reserved3 + '\'' +
                ", reserved2='" + reserved2 + '\'' +
                ", reserved1='" + reserved1 + '\'' +
                ", createdTs=" + createdTs +
                ", createdBy='" + createdBy + '\'' +
                ", updatedTs=" + updatedTs +
                ", updatedBy='" + updatedBy + '\'' +
                ", activeFlg='" + activeFlg + '\'' +
                '}';
    }
}
