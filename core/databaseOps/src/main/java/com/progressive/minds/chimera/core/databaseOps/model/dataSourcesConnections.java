package com.progressive.minds.chimera.core.databaseOps.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Represents a Chimera Data Source Connection entity.
 */
public class dataSourcesConnections {

    private String dataSourceConnectionName;
    private String dataSourceType;
    private String dataSourceSubType;
    private String host;
    private Integer port;
    private String databaseName;
    private String schemaName;
    private String authenticationType;
    private String userName;
    private String userPassword;
    private String role;
    private String warehouse;
    private String principal;
    private String keytab;
    private String sslCert;
    private String sslKey;
    private String sslRootCert;
    private String token;
    private String kafkaBroker;
    private String kafkaKeystoreType;
    private String kafkaKeystoreLocation;
    private String kafkaKeystorePassword;
    private String kafkaTruststoreType;
    private String kafkaTruststoreLocation;
    private String kafkaTruststorePassword;
    private String kafkaKeyPassword;
    private Timestamp createdTimestamp;
    private String createdBy;
    private Timestamp updatedTimestamp;
    private String updatedBy;
    private String activeFlag;

    public dataSourcesConnections() {}

    public dataSourcesConnections(String dataSourceConnectionName, String dataSourceType, String dataSourceSubType,
                                  String host, Integer port, String databaseName, String schemaName,
                                  String authenticationType, String userName, String userPassword, String role,
                                  String warehouse, String principal, String keytab, String sslCert, String sslKey,
                                  String sslRootCert, String token, String kafkaBroker, String kafkaKeystoreType,
                                  String kafkaKeystoreLocation, String kafkaKeystorePassword,
                                  String kafkaTruststoreType, String kafkaTruststoreLocation, String kafkaTruststorePassword,
                                  String kafkaKeyPassword, Timestamp createdTimestamp, String createdBy,
                                  Timestamp updatedTimestamp, String updatedBy, String activeFlag) {
        this.dataSourceConnectionName = dataSourceConnectionName;
        this.dataSourceType = dataSourceType;
        this.dataSourceSubType = dataSourceSubType;
        this.host = host;
        this.port = port;
        this.databaseName = databaseName;
        this.schemaName = schemaName;
        this.authenticationType = authenticationType;
        this.userName = userName;
        this.userPassword = userPassword;
        this.role = role;
        this.warehouse = warehouse;
        this.principal = principal;
        this.keytab = keytab;
        this.sslCert = sslCert;
        this.sslKey = sslKey;
        this.sslRootCert = sslRootCert;
        this.token = token;
        this.kafkaBroker = kafkaBroker;
        this.kafkaKeystoreType = kafkaKeystoreType;
        this.kafkaKeystoreLocation = kafkaKeystoreLocation;
        this.kafkaKeystorePassword = kafkaKeystorePassword;
        this.kafkaTruststoreType = kafkaTruststoreType;
        this.kafkaTruststoreLocation = kafkaTruststoreLocation;
        this.kafkaTruststorePassword = kafkaTruststorePassword;
        this.kafkaKeyPassword = kafkaKeyPassword;
        this.createdTimestamp = createdTimestamp;
        this.createdBy = createdBy;
        this.updatedTimestamp = updatedTimestamp;
        this.updatedBy = updatedBy;
        this.activeFlag = activeFlag;
    }

    // Getters and Setters

    // Getters and setters for all fields

    public String getDataSourceConnectionName() {
        return dataSourceConnectionName;
    }

    public void setDataSourceConnectionName(String dataSourceConnectionName) {
        this.dataSourceConnectionName = dataSourceConnectionName;
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getKeytab() {
        return keytab;
    }

    public void setKeytab(String keytab) {
        this.keytab = keytab;
    }

    public String getSslCert() {
        return sslCert;
    }

    public void setSslCert(String sslCert) {
        this.sslCert = sslCert;
    }

    public String getSslKey() {
        return sslKey;
    }

    public void setSslKey(String sslKey) {
        this.sslKey = sslKey;
    }

    public String getSslRootCert() {
        return sslRootCert;
    }

    public void setSslRootCert(String sslRootCert) {
        this.sslRootCert = sslRootCert;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getKafkaBroker() {
        return kafkaBroker;
    }

    public void setKafkaBroker(String kafkaBroker) {
        this.kafkaBroker = kafkaBroker;
    }

    public String getKafkaKeystoreType() {
        return kafkaKeystoreType;
    }

    public void setKafkaKeystoreType(String kafkaKeystoreType) {
        this.kafkaKeystoreType = kafkaKeystoreType;
    }

    public String getKafkaKeystoreLocation() {
        return kafkaKeystoreLocation;
    }

    public void setKafkaKeystoreLocation(String kafkaKeystoreLocation) {
        this.kafkaKeystoreLocation = kafkaKeystoreLocation;
    }

    public String getKafkaKeystorePassword() {
        return kafkaKeystorePassword;
    }

    public void setKafkaKeystorePassword(String kafkaKeystorePassword) {
        this.kafkaKeystorePassword = kafkaKeystorePassword;
    }

    public String getKafkaTruststoreType() {
        return kafkaTruststoreType;
    }

    public void setKafkaTruststoreType(String kafkaTruststoreType) {
        this.kafkaTruststoreType = kafkaTruststoreType;
    }

    public String getKafkaTruststoreLocation() {
        return kafkaTruststoreLocation;
    }

    public void setKafkaTruststoreLocation(String kafkaTruststoreLocation) {
        this.kafkaTruststoreLocation = kafkaTruststoreLocation;
    }

    public String getKafkaTruststorePassword() {
        return kafkaTruststorePassword;
    }

    public void setKafkaTruststorePassword(String kafkaTruststorePassword) {
        this.kafkaTruststorePassword = kafkaTruststorePassword;
    }

    public String getKafkaKeyPassword() {
        return kafkaKeyPassword;
    }

    public void setKafkaKeyPassword(String kafkaKeyPassword) {
        this.kafkaKeyPassword = kafkaKeyPassword;
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
        return "ChimeraDataSourceConnection{" +
                "dataSourceConnectionName='" + dataSourceConnectionName + '\'' +
                ", dataSourceType='" + dataSourceType + '\'' +
                ", dataSourceSubType='" + dataSourceSubType + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", databaseName='" + databaseName + '\'' +
                ", schemaName='" + schemaName + '\'' +
                ", authenticationType='" + authenticationType + '\'' +
                ", userName='" + userName + '\'' +
                ", userPassword='******' " +
                ", role='" + role + '\'' +
                ", warehouse='" + warehouse + '\'' +
                ", principal='" + principal + '\'' +
                ", keytab='" + keytab + '\'' +
                ", sslCert='" + sslCert + '\'' +
                ", sslKey='" + sslKey + '\'' +
                ", sslRootCert='" + sslRootCert + '\'' +
                ", token='" + token + '\'' +
                ", kafkaBroker='" + kafkaBroker + '\'' +
                ", kafkaKeystoreType='" + kafkaKeystoreType + '\'' +
                ", kafkaKeystoreLocation='" + kafkaKeystoreLocation + '\'' +
                ", kafkaTruststoreType='" + kafkaTruststoreType + '\'' +
                ", kafkaTruststoreLocation='" + kafkaTruststoreLocation + '\'' +
                ", kafkaTruststorePassword='******' " +
                ", kafkaKeyPassword='******' " +
                ", createdTimestamp=" + createdTimestamp +
                ", createdBy='" + createdBy + '\'' +
                ", updatedTimestamp=" + updatedTimestamp +
                ", updatedBy='" + updatedBy + '\'' +
                ", activeFlag='" + activeFlag + '\'' +
                '}';
    }
}

