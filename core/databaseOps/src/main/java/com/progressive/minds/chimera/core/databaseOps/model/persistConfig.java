package com.progressive.minds.chimera.core.databaseOps.model;


import java.sql.Timestamp;

/**
 * Represents a Persist Configuration entity.
 */
public class persistConfig {

    private int uniqueId; // Unique identifier
    private String pipelineName; // Name of the associated pipeline
    private Integer sequenceNumber; // Sequence number for execution
    private String dataSinkType; // Type of the data sink
    private String dataSinkSubType; // Subtype of the data sink
    private String targetDatabaseName; // Name of the target database
    private String targetTableName; // Name of the target table
    private String targetSchemaName; // Name of the target schema
    private String partitionKeys; // Partition keys for the target
    private String targetSqlText; // Target SQL query text
    private String targetPath; // Target file path
    private String writeMode; // Write mode ('Overwrite' or 'Append')
    private String dataSourceConnectionName; // Data source connection name
    private String sinkConfiguration; // Sink configuration in JSON format
    private String sortColumns; // Columns to sort data
    private String dedupColumns; // Columns for deduplication
    private String kafkaTopic; // Kafka topic name
    private String kafkaKey; // Kafka key
    private String kafkaMessage; // Kafka message
    private Timestamp createdTimestamp; // Record creation timestamp
    private String createdBy; // Record created by
    private Timestamp updatedTimestamp; // Record update timestamp
    private String updatedBy; // Record updated by
    private String activeFlag; // Denotes if the record is active ('Y' or 'N')

    public persistConfig() {
    }

    public persistConfig(int uniqueId, String pipelineName, Integer sequenceNumber, String dataSinkType, String dataSinkSubType,
                         String targetDatabaseName, String targetTableName, String targetSchemaName, String partitionKeys,
                         String targetSqlText, String targetPath, String writeMode, String dataSourceConnectionName,
                         String sinkConfiguration, String sortColumns, String dedupColumns, String kafkaTopic,
                         String kafkaKey, String kafkaMessage, Timestamp createdTimestamp, String createdBy,
                         Timestamp updatedTimestamp, String updatedBy, String activeFlag) {
        this.uniqueId = uniqueId;
        this.pipelineName = pipelineName;
        this.sequenceNumber = sequenceNumber;
        this.dataSinkType = dataSinkType;
        this.dataSinkSubType = dataSinkSubType;
        this.targetDatabaseName = targetDatabaseName;
        this.targetTableName = targetTableName;
        this.targetSchemaName = targetSchemaName;
        this.partitionKeys = partitionKeys;
        this.targetSqlText = targetSqlText;
        this.targetPath = targetPath;
        this.writeMode = writeMode;
        this.dataSourceConnectionName = dataSourceConnectionName;
        this.sinkConfiguration = sinkConfiguration;
        this.sortColumns = sortColumns;
        this.dedupColumns = dedupColumns;
        this.kafkaTopic = kafkaTopic;
        this.kafkaKey = kafkaKey;
        this.kafkaMessage = kafkaMessage;
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

    public String getDataSinkType() {
        return dataSinkType;
    }

    public void setDataSinkType(String dataSinkType) {
        this.dataSinkType = dataSinkType;
    }

    public String getDataSinkSubType() {
        return dataSinkSubType;
    }

    public void setDataSinkSubType(String dataSinkSubType) {
        this.dataSinkSubType = dataSinkSubType;
    }

    public String getTargetDatabaseName() {
        return targetDatabaseName;
    }

    public void setTargetDatabaseName(String targetDatabaseName) {
        this.targetDatabaseName = targetDatabaseName;
    }

    public String getTargetTableName() {
        return targetTableName;
    }

    public void setTargetTableName(String targetTableName) {
        this.targetTableName = targetTableName;
    }

    public String getTargetSchemaName() {
        return targetSchemaName;
    }

    public void setTargetSchemaName(String targetSchemaName) {
        this.targetSchemaName = targetSchemaName;
    }

    public String getPartitionKeys() {
        return partitionKeys;
    }

    public void setPartitionKeys(String partitionKeys) {
        this.partitionKeys = partitionKeys;
    }

    public String getTargetSqlText() {
        return targetSqlText;
    }

    public void setTargetSqlText(String targetSqlText) {
        this.targetSqlText = targetSqlText;
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

    public String getDataSourceConnectionName() {
        return dataSourceConnectionName;
    }

    public void setDataSourceConnectionName(String dataSourceConnectionName) {
        this.dataSourceConnectionName = dataSourceConnectionName;
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
        return "PersistConfig{" +
                "uniqueId=" + uniqueId +
                ", pipelineName='" + pipelineName + '\'' +
                ", sequenceNumber=" + sequenceNumber +
                ", dataSinkType='" + dataSinkType + '\'' +
                ", dataSinkSubType='" + dataSinkSubType + '\'' +
                ", targetDatabaseName='" + targetDatabaseName + '\'' +
                ", targetTableName='" + targetTableName + '\'' +
                ", targetSchemaName='" + targetSchemaName + '\'' +
                ", partitionKeys='" + partitionKeys + '\'' +
                ", targetSqlText='" + targetSqlText + '\'' +
                ", targetPath='" + targetPath + '\'' +
                ", writeMode='" + writeMode + '\'' +
                ", dataSourceConnectionName='" + dataSourceConnectionName + '\'' +
                ", sinkConfiguration=" + sinkConfiguration +
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
