package com.progressive.minds.chimera.core.databaseOps.model.metadata;

public class persistView {
    private String pipelineName;
    private Integer sequenceNumber;
    private String dataSinkType;
    private String dataSinkSubType;
    private String targetDatabaseName;
    private String targetTableName;
    private String targetSchemaName;
    private String partitionKeys;
    private String targetSqlText;
    private String targetPath;
    private String writeMode;
    private String dataSourceConnectionName;
    private String sinkConfiguration;
    private String sortColumns;
    private String dedupColumns;
    private String kafkaTopic;
    private String kafkaKey;
    private String kafkaMessage;
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
    private String kafkaKeyPassword;

    public String getPipelineName() {
        return pipelineName;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public String getDataSinkType() {
        return dataSinkType;
    }

    public String getDataSinkSubType() {
        return dataSinkSubType;
    }

    public String getTargetDatabaseName() {
        return targetDatabaseName;
    }

    public String getTargetTableName() {
        return targetTableName;
    }

    public String getTargetSchemaName() {
        return targetSchemaName;
    }

    public String getPartitionKeys() {
        return partitionKeys;
    }

    public String getTargetSqlText() {
        return targetSqlText;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public String getWriteMode() {
        return writeMode;
    }

    public String getDataSourceConnectionName() {
        return dataSourceConnectionName;
    }

    public String getSinkConfiguration() {
        return sinkConfiguration;
    }

    public String getSortColumns() {
        return sortColumns;
    }

    public String getDedupColumns() {
        return dedupColumns;
    }

    public String getKafkaTopic() {
        return kafkaTopic;
    }

    public String getKafkaKey() {
        return kafkaKey;
    }

    public String getKafkaMessage() {
        return kafkaMessage;
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

    @Override
    public String toString() {
        return "persistView{" +
                "pipelineName='" + pipelineName + '\'' +
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
                ", sinkConfiguration='" + sinkConfiguration + '\'' +
                ", sortColumns='" + sortColumns + '\'' +
                ", dedupColumns='" + dedupColumns + '\'' +
                ", kafkaTopic='" + kafkaTopic + '\'' +
                ", kafkaKey='" + kafkaKey + '\'' +
                ", kafkaMessage='" + kafkaMessage + '\'' +
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
}
