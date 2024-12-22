package com.progressive.minds.chimera.core.databaseOps.model.metadata.updated;

import java.util.UUID;
import java.sql.Timestamp;

public class FileExtractMetadataConfig extends ExtractMetadataConfig {

    private String fileName;
    private String filePath;
    private String delimiter;
    private String qualifier;
    private long sizeInByte;
    private String compressionType;
    private String schemaPath;
    private String rowFilter;
    private String columnFilter;
    private String activeFlag;

    // Getters and Setters

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public long getSizeInByte() {
        return sizeInByte;
    }

    public void setSizeInByte(long sizeInByte) {
        this.sizeInByte = sizeInByte;
    }

    public String getCompressionType() {
        return compressionType;
    }

    public void setCompressionType(String compressionType) {
        this.compressionType = compressionType;
    }

    public String getSchemaPath() {
        return schemaPath;
    }

    public void setSchemaPath(String schemaPath) {
        this.schemaPath = schemaPath;
    }

    public String getRowFilter() {
        return rowFilter;
    }

    public void setRowFilter(String rowFilter) {
        this.rowFilter = rowFilter;
    }

    public String getColumnFilter() {
        return columnFilter;
    }

    public void setColumnFilter(String columnFilter) {
        this.columnFilter = columnFilter;
    }

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }

    @Override
    public String toString() {
        return "FileExtractMetadataConfig{" +
                "fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", delimiter='" + delimiter + '\'' +
                ", qualifier='" + qualifier + '\'' +
                ", sizeInByte=" + sizeInByte +
                ", compressionType='" + compressionType + '\'' +
                ", schemaPath='" + schemaPath + '\'' +
                ", rowFilter='" + rowFilter + '\'' +
                ", columnFilter='" + columnFilter + '\'' +
                ", activeFlag='" + activeFlag + '\'' +
                "} " + super.toString();
    }
}
