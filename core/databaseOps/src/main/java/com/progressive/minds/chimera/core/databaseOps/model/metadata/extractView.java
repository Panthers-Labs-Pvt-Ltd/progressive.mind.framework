package com.progressive.minds.chimera.core.databaseOps.model.metadata;

public class extractView {
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
    private String readDefaults;
    private String writeDefaults;
    private String host;
    private Integer port;
    private String connectionDatabaseName;
    private String connectionSchemaName;
    private String authenticationType;
    private String userName;
    private String userPassword;
    private String cloudProvider;
    private String secretName;
    private String gcpProjectId;
    private String azureKeyVaultUrl;
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

    @Override
    public String toString() {
        return "extractView{" +
                "pipelineName='" + pipelineName + '\'' +
                ", sequenceNumber=" + sequenceNumber +
                ", dataSourceType='" + dataSourceType + '\'' +
                ", dataSourceSubType='" + dataSourceSubType + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", schemaPath='" + schemaPath + '\'' +
                ", rowFilter='" + rowFilter + '\'' +
                ", columnFilter='" + columnFilter + '\'' +
                ", extractDataframeName='" + extractDataframeName + '\'' +
                ", sourceConfiguration='" + sourceConfiguration + '\'' +
                ", tableName='" + tableName + '\'' +
                ", schemaName='" + schemaName + '\'' +
                ", sqlText='" + sqlText + '\'' +
                ", kafkaConsumerTopic='" + kafkaConsumerTopic + '\'' +
                ", kafkaConsumerGroup='" + kafkaConsumerGroup + '\'' +
                ", kafkaStartOffset='" + kafkaStartOffset + '\'' +
                ", dataSourceConnectionName='" + dataSourceConnectionName + '\'' +
                ", readDefaults='" + readDefaults + '\'' +
                ", writeDefaults='" + writeDefaults + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", connectionDatabaseName='" + connectionDatabaseName + '\'' +
                ", connectionSchemaName='" + connectionSchemaName + '\'' +
                ", authenticationType='" + authenticationType + '\'' +
                ", userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", cloudProvider='" + cloudProvider + '\'' +
                ", secretName='" + secretName + '\'' +
                ", gcpProjectId='" + gcpProjectId + '\'' +
                ", azureKeyVaultUrl='" + azureKeyVaultUrl + '\'' +
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
                ", kafkaKeystorePassword='" + kafkaKeystorePassword + '\'' +
                ", kafkaTruststoreType='" + kafkaTruststoreType + '\'' +
                ", kafkaTruststoreLocation='" + kafkaTruststoreLocation + '\'' +
                ", kafkaTruststorePassword='" + kafkaTruststorePassword + '\'' +
                ", kafkaKeyPassword='" + kafkaKeyPassword + '\'' +
                '}';
    }

    private String kafkaKeyPassword;

    public String getPipelineName() {
        return pipelineName;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public String getDataSourceType() {
        return dataSourceType;
    }

    public String getDataSourceSubType() {
        return dataSourceSubType;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getSchemaPath() {
        return schemaPath;
    }

    public String getRowFilter() {
        return rowFilter;
    }

    public String getColumnFilter() {
        return columnFilter;
    }

    public String getExtractDataframeName() {
        return extractDataframeName;
    }

    public String getSourceConfiguration() {
        return sourceConfiguration;
    }

    public String getTableName() {
        return tableName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public String getSqlText() {
        return sqlText;
    }

    public String getKafkaConsumerTopic() {
        return kafkaConsumerTopic;
    }

    public String getKafkaConsumerGroup() {
        return kafkaConsumerGroup;
    }

    public String getKafkaStartOffset() {
        return kafkaStartOffset;
    }

    public String getDataSourceConnectionName() {
        return dataSourceConnectionName;
    }

    public String getReadDefaults() {
        return readDefaults;
    }

    public String getWriteDefaults() {
        return writeDefaults;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getConnectionDatabaseName() {
        return connectionDatabaseName;
    }

    public String getConnectionSchemaName() {
        return connectionSchemaName;
    }

    public String getAuthenticationType() {
        return authenticationType;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getCloudProvider() {
        return cloudProvider;
    }

    public String getSecretName() {
        return secretName;
    }

    public String getGcpProjectId() {
        return gcpProjectId;
    }

    public String getAzureKeyVaultUrl() {
        return azureKeyVaultUrl;
    }

    public String getRole() {
        return role;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public String getPrincipal() {
        return principal;
    }

    public String getKeytab() {
        return keytab;
    }

    public String getSslCert() {
        return sslCert;
    }

    public String getSslKey() {
        return sslKey;
    }

    public String getSslRootCert() {
        return sslRootCert;
    }

    public String getToken() {
        return token;
    }

    public String getKafkaBroker() {
        return kafkaBroker;
    }

    public String getKafkaKeystoreType() {
        return kafkaKeystoreType;
    }

    public String getKafkaKeystoreLocation() {
        return kafkaKeystoreLocation;
    }

    public String getKafkaKeystorePassword() {
        return kafkaKeystorePassword;
    }

    public String getKafkaTruststoreType() {
        return kafkaTruststoreType;
    }

    public String getKafkaTruststoreLocation() {
        return kafkaTruststoreLocation;
    }

    public String getKafkaTruststorePassword() {
        return kafkaTruststorePassword;
    }

    public String getKafkaKeyPassword() {
        return kafkaKeyPassword;
    }

}
