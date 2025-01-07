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

    public void setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public void setDataSinkType(String dataSinkType) {
        this.dataSinkType = dataSinkType;
    }

    public void setDataSinkSubType(String dataSinkSubType) {
        this.dataSinkSubType = dataSinkSubType;
    }

    public void setTargetDatabaseName(String targetDatabaseName) {
        this.targetDatabaseName = targetDatabaseName;
    }

    public void setTargetTableName(String targetTableName) {
        this.targetTableName = targetTableName;
    }

    public void setTargetSchemaName(String targetSchemaName) {
        this.targetSchemaName = targetSchemaName;
    }

    public void setPartitionKeys(String partitionKeys) {
        this.partitionKeys = partitionKeys;
    }

    public void setTargetSqlText(String targetSqlText) {
        this.targetSqlText = targetSqlText;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public void setWriteMode(String writeMode) {
        this.writeMode = writeMode;
    }

    public void setDataSourceConnectionName(String dataSourceConnectionName) {
        this.dataSourceConnectionName = dataSourceConnectionName;
    }

    public void setSinkConfiguration(String sinkConfiguration) {
        this.sinkConfiguration = sinkConfiguration;
    }

    public void setSortColumns(String sortColumns) {
        this.sortColumns = sortColumns;
    }

    public void setDedupColumns(String dedupColumns) {
        this.dedupColumns = dedupColumns;
    }

    public void setKafkaTopic(String kafkaTopic) {
        this.kafkaTopic = kafkaTopic;
    }

    public void setKafkaKey(String kafkaKey) {
        this.kafkaKey = kafkaKey;
    }

    public void setKafkaMessage(String kafkaMessage) {
        this.kafkaMessage = kafkaMessage;
    }

    public void setReadDefaults(String readDefaults) {
        this.readDefaults = readDefaults;
    }

    public void setWriteDefaults(String writeDefaults) {
        this.writeDefaults = writeDefaults;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setConnectionDatabaseName(String connectionDatabaseName) {
        this.connectionDatabaseName = connectionDatabaseName;
    }

    public void setConnectionSchemaName(String connectionSchemaName) {
        this.connectionSchemaName = connectionSchemaName;
    }

    public void setAuthenticationType(String authenticationType) {
        this.authenticationType = authenticationType;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setCloudProvider(String cloudProvider) {
        this.cloudProvider = cloudProvider;
    }

    public void setSecretName(String secretName) {
        this.secretName = secretName;
    }

    public void setGcpProjectId(String gcpProjectId) {
        this.gcpProjectId = gcpProjectId;
    }

    public void setAzureKeyVaultUrl(String azureKeyVaultUrl) {
        this.azureKeyVaultUrl = azureKeyVaultUrl;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public void setKeytab(String keytab) {
        this.keytab = keytab;
    }

    public void setSslCert(String sslCert) {
        this.sslCert = sslCert;
    }

    public void setSslKey(String sslKey) {
        this.sslKey = sslKey;
    }

    public void setSslRootCert(String sslRootCert) {
        this.sslRootCert = sslRootCert;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setKafkaBroker(String kafkaBroker) {
        this.kafkaBroker = kafkaBroker;
    }

    public void setKafkaKeystoreType(String kafkaKeystoreType) {
        this.kafkaKeystoreType = kafkaKeystoreType;
    }

    public void setKafkaKeystoreLocation(String kafkaKeystoreLocation) {
        this.kafkaKeystoreLocation = kafkaKeystoreLocation;
    }

    public void setKafkaKeystorePassword(String kafkaKeystorePassword) {
        this.kafkaKeystorePassword = kafkaKeystorePassword;
    }

    public void setKafkaTruststoreType(String kafkaTruststoreType) {
        this.kafkaTruststoreType = kafkaTruststoreType;
    }

    public void setKafkaTruststoreLocation(String kafkaTruststoreLocation) {
        this.kafkaTruststoreLocation = kafkaTruststoreLocation;
    }

    public void setKafkaTruststorePassword(String kafkaTruststorePassword) {
        this.kafkaTruststorePassword = kafkaTruststorePassword;
    }

    public void setKafkaKeyPassword(String kafkaKeyPassword) {
        this.kafkaKeyPassword = kafkaKeyPassword;
    }

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
