package com.progressive.minds.chimera.core.databaseOps.model.metadata;

import java.sql.Timestamp;


/**
 * Represents an Extract Configuration entity.
 */
public class extractConfig {

    private int uniqueId;
    private String pipelineName;
    private Integer sequenceNumber;
    private String dataSourceType;
    private String dataSourceSubType;
    private String fileName;
    private String filePath;
    private String schemaPath;
    private String rowFilter;
    private String columnFilter;
    private String extractDataframeName;
    private String sourceConfiguration;
    private String tableName;
    private String schemaName;
    private String sqlText;
    private String kafkaConsumerTopic;
    private String kafkaConsumerGroup;
    private String kafkaStartOffset;
    private String dataSourceConnectionName;
    private Timestamp createdTimestamp;
    private String createdBy;
    private Timestamp updatedTimestamp;
    private String updatedBy;
    private String activeFlag;

    public extractConfig() {
    }

    public extractConfig(int uniqueId, String pipelineName, Integer sequenceNumber, String dataSourceType,
                         String dataSourceSubType, String fileName, String filePath, String schemaPath,
                         String rowFilter, String columnFilter, String extractDataframeName, String sourceConfiguration,
                         String tableName, String schemaName, String sqlText, String kafkaConsumerTopic, String kafkaConsumerGroup,
                         String kafkaStartOffset, String dataSourceConnectionName, Timestamp createdTimestamp,
                         String createdBy, Timestamp updatedTimestamp, String updatedBy, String activeFlag) {
        this.uniqueId = uniqueId;
        this.pipelineName = pipelineName;
        this.sequenceNumber = sequenceNumber;
        this.dataSourceType = dataSourceType;
        this.dataSourceSubType = dataSourceSubType;
        this.fileName = fileName;
        this.filePath = filePath;
        this.schemaPath = schemaPath;
        this.rowFilter = rowFilter;
        this.columnFilter = columnFilter;
        this.extractDataframeName = extractDataframeName;
        this.sourceConfiguration = sourceConfiguration;
        this.tableName = tableName;
        this.schemaName = schemaName;
        this.sqlText = sqlText;
        this.kafkaConsumerTopic = kafkaConsumerTopic;
        this.kafkaConsumerGroup = kafkaConsumerGroup;
        this.kafkaStartOffset = kafkaStartOffset;
        this.dataSourceConnectionName = dataSourceConnectionName;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSchemaPath() {
        return schemaPath;
    }

    public void setSchemaPath(String schemaPath) {
        this.schemaPath = schemaPath;
    }

    public String getRowFilter() {
        return rowFilter;
    }

    public void setRowFilter(String rowFilter) {
        this.rowFilter = rowFilter;
    }

    public String getColumnFilter() {
        return columnFilter;
    }

    public void setColumnFilter(String columnFilter) {
        this.columnFilter = columnFilter;
    }

    public String getExtractDataframeName() {
        return extractDataframeName;
    }

    public void setExtractDataframeName(String extractDataframeName) {
        this.extractDataframeName = extractDataframeName;
    }

    public String getSourceConfiguration() {
        return sourceConfiguration;
    }

    public void setSourceConfiguration(String sourceConfiguration) {
        this.sourceConfiguration = sourceConfiguration;
    }

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

    public String getKafkaConsumerTopic() {
        return kafkaConsumerTopic;
    }

    public void setKafkaConsumerTopic(String kafkaConsumerTopic) {
        this.kafkaConsumerTopic = kafkaConsumerTopic;
    }

    public String getKafkaConsumerGroup() {
        return kafkaConsumerGroup;
    }

    public void setKafkaConsumerGroup(String kafkaConsumerGroup) {
        this.kafkaConsumerGroup = kafkaConsumerGroup;
    }

    public String getKafkaStartOffset() {
        return kafkaStartOffset;
    }

    public void setKafkaStartOffset(String kafkaStartOffset) {
        this.kafkaStartOffset = kafkaStartOffset;
    }

    public String getDataSourceConnectionName() {
        return dataSourceConnectionName;
    }

    public void setDataSourceConnectionName(String dataSourceConnectionName) {
        this.dataSourceConnectionName = dataSourceConnectionName;
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
        return "ExtractConfig{" +
                "uniqueId=" + uniqueId +
                ", pipelineName='" + pipelineName + '\'' +
                ", sequenceNumber=" + sequenceNumber +
                ", dataSourceType='" + dataSourceType + '\'' +
                ", dataSourceSubType='" + dataSourceSubType + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", schemaPath='" + schemaPath + '\'' +
                ", rowFilter='" + rowFilter + '\'' +
                ", columnFilter='" + columnFilter + '\'' +
                ", extractDataframeName='" + extractDataframeName + '\'' +
                ", sourceConfiguration=" + sourceConfiguration +
                ", tableName='" + tableName + '\'' +
                ", schemaName='" + schemaName + '\'' +
                ", sqlText='" + sqlText + '\'' +
                ", kafkaConsumerTopic='" + kafkaConsumerTopic + '\'' +
                ", kafkaConsumerGroup='" + kafkaConsumerGroup + '\'' +
                ", kafkaStartOffset='" + kafkaStartOffset + '\'' +
                ", dataSourceConnectionName='" + dataSourceConnectionName + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                ", createdBy='" + createdBy + '\'' +
                ", updatedTimestamp=" + updatedTimestamp +
                ", updatedBy='" + updatedBy + '\'' +
                ", activeFlag='" + activeFlag + '\'' +
                '}';
    }
}

