package com.progressive.minds.chimera.core.databaseOps.model.metadata;

public class DataLineage {

    private long rowNum;
    private String batchId;
    private String pipelineNm;
    private String databaseNm;
    private String tableNm;
    private String namespace;
    private String lineageJson;
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

    // Getters and setters
    public long getRowNum() {
        return rowNum;
    }

    public void setRowNum(long rowNum) {
        this.rowNum = rowNum;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getPipelineNm() {
        return pipelineNm;
    }

    public void setPipelineNm(String pipelineNm) {
        this.pipelineNm = pipelineNm;
    }

    public String getDatabaseNm() {
        return databaseNm;
    }

    public void setDatabaseNm(String databaseNm) {
        this.databaseNm = databaseNm;
    }

    public String getTableNm() {
        return tableNm;
    }

    public void setTableNm(String tableNm) {
        this.tableNm = tableNm;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getLineageJson() {
        return lineageJson;
    }

    public void setLineageJson(String lineageJson) {
        this.lineageJson = lineageJson;
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
}
