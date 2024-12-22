package com.progressive.minds.chimera.core.databaseOps.model.metadata.updated;

import java.util.UUID;
import java.sql.Timestamp;

public class DataSourceConnection {

    private UUID connectionId;
    private String connectionName;
    private UUID dataSourceId;
    private String authenticationType;
    private String authenticationData;
    private String connectionMetadata;
    private String userReadConfig;
    private String userConfig;
    private String description;
    private Timestamp lastUpdated;
    private Timestamp createdAt;
    private String status;

    // Getters and Setters

    public UUID getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(UUID connectionId) {
        this.connectionId = connectionId;
    }

    public String getConnectionName() {
        return connectionName;
    }

    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    public UUID getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(UUID dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
    }

    public String getAuthenticationData() {
        return authenticationData;
    }

    public void setAuthenticationData(String authenticationData) {
        this.authenticationData = authenticationData;
    }

    public String getConnectionMetadata() {
        return connectionMetadata;
    }

    public void setConnectionMetadata(String connectionMetadata) {
        this.connectionMetadata = connectionMetadata;
    }

    public String getUserReadConfig() {
        return userReadConfig;
    }

    public void setUserReadConfig(String userReadConfig) {
        this.userReadConfig = userReadConfig;
    }

    public String getUserConfig() {
        return userConfig;
    }

    public void setUserConfig(String userConfig) {
        this.userConfig = userConfig;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DataSourceConnection{" +
                "connectionId=" + connectionId +
                ", connectionName='" + connectionName + '\'' +
                ", dataSourceId=" + dataSourceId +
                ", authenticationType='" + authenticationType + '\'' +
                ", authenticationData='" + authenticationData + '\'' +
                ", connectionMetadata='" + connectionMetadata + '\'' +
                ", userReadConfig='" + userReadConfig + '\'' +
                ", userConfig='" + userConfig + '\'' +
                ", description='" + description + '\'' +
                ", lastUpdated=" + lastUpdated +
                ", createdAt=" + createdAt +
                ", status='" + status + '\'' +
                '}';
    }
}
