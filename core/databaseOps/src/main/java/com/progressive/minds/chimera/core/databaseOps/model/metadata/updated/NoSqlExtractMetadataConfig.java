package com.progressive.minds.chimera.core.databaseOps.model.metadata.updated;

import java.util.UUID;

public class NoSqlExtractMetadataConfig extends ExtractMetadataConfig {

    private String uri;
    private String collection;
    private String readPreference;
    private String partitioner;
    private String activeFlag;

    // Getters and Setters

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getReadPreference() {
        return readPreference;
    }

    public void setReadPreference(String readPreference) {
        this.readPreference = readPreference;
    }

    public String getPartitioner() {
        return partitioner;
    }

    public void setPartitioner(String partitioner) {
        this.partitioner = partitioner;
    }

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }

    @Override
    public String toString() {
        return "NoSqlExtractMetadataConfig{" +
                "uri='" + uri + '\'' +
                ", collection='" + collection + '\'' +
                ", readPreference='" + readPreference + '\'' +
                ", partitioner='" + partitioner + '\'' +
                ", activeFlag='" + activeFlag + '\'' +
                "} " + super.toString();
    }
}
