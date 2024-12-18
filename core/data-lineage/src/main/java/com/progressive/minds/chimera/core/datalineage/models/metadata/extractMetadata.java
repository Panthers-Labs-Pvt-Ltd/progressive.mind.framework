package com.progressive.minds.chimera.core.datalineage.models.metadata;

import java.sql.Timestamp;

public class extractMetadata {
    private String nameSpace;
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
    private String dataSourceConnectionName;



    public String getNameSpace() {
        return pipelineName;
    }
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
    public String getDataSourceConnectionName() {
        return dataSourceConnectionName;
    }
}
