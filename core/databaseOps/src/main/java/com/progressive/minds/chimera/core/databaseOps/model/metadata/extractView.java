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
    private String kafkaKeyPassword;

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

    public void setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public void setDataSourceType(String dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public void setDataSourceSubType(String dataSourceSubType) {
        this.dataSourceSubType = dataSourceSubType;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setSchemaPath(String schemaPath) {
        this.schemaPath = schemaPath;
    }

    public void setRowFilter(String rowFilter) {
        this.rowFilter = rowFilter;
    }

    public void setColumnFilter(String columnFilter) {
        this.columnFilter = columnFilter;
    }

    public void setExtractDataframeName(String extractDataframeName) {
        this.extractDataframeName = extractDataframeName;
    }

    public void setSourceConfiguration(String sourceConfiguration) {
        this.sourceConfiguration = sourceConfiguration;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public void setSqlText(String sqlText) {
        this.sqlText = sqlText;
    }

    public void setKafkaConsumerTopic(String kafkaConsumerTopic) {
        this.kafkaConsumerTopic = kafkaConsumerTopic;
    }

    public void setKafkaConsumerGroup(String kafkaConsumerGroup) {
        this.kafkaConsumerGroup = kafkaConsumerGroup;
    }

    public void setKafkaStartOffset(String kafkaStartOffset) {
        this.kafkaStartOffset = kafkaStartOffset;
    }

    public void setDataSourceConnectionName(String dataSourceConnectionName) {
        this.dataSourceConnectionName = dataSourceConnectionName;
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
}
