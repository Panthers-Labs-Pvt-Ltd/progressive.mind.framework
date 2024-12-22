package com.progressive.minds.chimera.core.databaseOps.model.metadata.updated;

import java.util.UUID;

public class PersistMetadataConfig {

    private UUID persistId;
    private Integer sequenceNumber;
    private UUID pipelineId;
    private UUID dataSourceConnectionId;
    private String databaseName;
    private String tableName;
    private String schemaName;
    private String partitionKeys;
    private String targetSql;
    private String targetPath;
    private String writeMode;
    private String sinkConfiguration;
    private String sortColumns;
    private String dedupColumns;
    private String kafkaTopic;
    private String kafkaKey;
    private String kafkaMessage;
    private java.sql.Timestamp createdTimestamp;
    private String createdBy;
    private java.sql.Timestamp updatedTimestamp;
    private String updatedBy;
    private String activeFlag;

    // Getters and Setters

    public UUID getPersistId() {
        return persistId;
    }

    public void setPersistId(UUID persistId) {
        this.persistId = persistId;
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

    public UUID getDataSourceConnectionId() {
        return dataSourceConnectionId;
    }

    public void setDataSourceConnectionId(UUID dataSourceConnectionId) {
        this.dataSourceConnectionId = dataSourceConnectionId;
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

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getPartitionKeys() {
        return partitionKeys;
    }

    public void setPartitionKeys(String partitionKeys) {
        this.partitionKeys = partitionKeys;
    }

    public String getTargetSql() {
        return targetSql;
    }

    public void setTargetSql(String targetSql) {
        this.targetSql = targetSql;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public String getWriteMode() {
        return writeMode;
    }

    public void setWriteMode(String writeMode) {
        this.writeMode = writeMode;
    }

    public String getSinkConfiguration() {
        return sinkConfiguration;
    }

    public void setSinkConfiguration(String sinkConfiguration) {
        this.sinkConfiguration = sinkConfiguration;
    }

    public String getSortColumns() {
        return sortColumns;
    }

    public void setSortColumns(String sortColumns) {
        this.sortColumns = sortColumns;
    }

    public String getDedupColumns() {
        return dedupColumns;
    }

    public void setDedupColumns(String dedupColumns) {
        this.dedupColumns = dedupColumns;
    }

    public String getKafkaTopic() {
        return kafkaTopic;
    }

    public void setKafkaTopic(String kafkaTopic) {
        this.kafkaTopic = kafkaTopic;
    }

    public String getKafkaKey() {
        return kafkaKey;
    }

    public void setKafkaKey(String kafkaKey) {
        this.kafkaKey = kafkaKey;
    }

    public String getKafkaMessage() {
        return kafkaMessage;
    }

    public void setKafkaMessage(String kafkaMessage) {
        this.kafkaMessage = kafkaMessage;
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
        return "PersistMetadataConfig{" +
                "persistId=" + persistId +
                ", sequenceNumber=" + sequenceNumber +
                ", pipelineId=" + pipelineId +
                ", dataSourceConnectionId=" + dataSourceConnectionId +
                ", databaseName='" + databaseName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", schemaName='" + schemaName + '\'' +
                ", partitionKeys='" + partitionKeys + '\'' +
                ", targetSql='" + targetSql + '\'' +
                ", targetPath='" + targetPath + '\'' +
                ", writeMode='" + writeMode + '\'' +
                ", sinkConfiguration='" + sinkConfiguration + '\'' +
                ", sortColumns='" + sortColumns + '\'' +
                ", dedupColumns='" + dedupColumns + '\'' +
                ", kafkaTopic='" + kafkaTopic + '\'' +
                ", kafkaKey='" + kafkaKey + '\'' +
                ", kafkaMessage='" + kafkaMessage + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                ", createdBy='" + createdBy + '\'' +
                ", updatedTimestamp=" + updatedTimestamp +
                ", updatedBy='" + updatedBy + '\'' +
                ", activeFlag='" + activeFlag + '\'' +
                '}';
    }
}

