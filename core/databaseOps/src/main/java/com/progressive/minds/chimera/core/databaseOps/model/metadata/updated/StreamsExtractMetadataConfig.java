package com.progressive.minds.chimera.core.databaseOps.model.metadata.updated;

import java.util.UUID;

public class StreamsExtractMetadataConfig extends ExtractMetadataConfig {

    private String kafkaConsumerTopic;
    private String kafkaConsumerGroup;
    private String kafkaStartOffset;
    private String kafkaMaxOffset;
    private Integer kafkaPollTimeout;
    private String txnConsumerFlag;
    private String watermarkDuration;
    private String storageFormat;
    private String storagePath;
    private String storagePartitions;
    private String activeFlag;

    // Getters and Setters

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

    public String getKafkaMaxOffset() {
        return kafkaMaxOffset;
    }

    public void setKafkaMaxOffset(String kafkaMaxOffset) {
        this.kafkaMaxOffset = kafkaMaxOffset;
    }

    public Integer getKafkaPollTimeout() {
        return kafkaPollTimeout;
    }

    public void setKafkaPollTimeout(Integer kafkaPollTimeout) {
        this.kafkaPollTimeout = kafkaPollTimeout;
    }

    public String getTxnConsumerFlag() {
        return txnConsumerFlag;
    }

    public void setTxnConsumerFlag(String txnConsumerFlag) {
        this.txnConsumerFlag = txnConsumerFlag;
    }

    public String getWatermarkDuration() {
        return watermarkDuration;
    }

    public void setWatermarkDuration(String watermarkDuration) {
        this.watermarkDuration = watermarkDuration;
    }

    public String getStorageFormat() {
        return storageFormat;
    }

    public void setStorageFormat(String storageFormat) {
        this.storageFormat = storageFormat;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public String getStoragePartitions() {
        return storagePartitions;
    }

    public void setStoragePartitions(String storagePartitions) {
        this.storagePartitions = storagePartitions;
    }

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }

    @Override
    public String toString() {
        return "StreamsExtractMetadataConfig{" +
                "kafkaConsumerTopic='" + kafkaConsumerTopic + '\'' +
                ", kafkaConsumerGroup='" + kafkaConsumerGroup + '\'' +
                ", kafkaStartOffset='" + kafkaStartOffset + '\'' +
                ", kafkaMaxOffset='" + kafkaMaxOffset + '\'' +
                ", kafkaPollTimeout=" + kafkaPollTimeout +
                ", txnConsumerFlag='" + txnConsumerFlag + '\'' +
                ", watermarkDuration='" + watermarkDuration + '\'' +
                ", storageFormat='" + storageFormat + '\'' +
                ", storagePath='" + storagePath + '\'' +
                ", storagePartitions='" + storagePartitions + '\'' +
                ", activeFlag='" + activeFlag + '\'' +
                "} " + super.toString();
    }
}

