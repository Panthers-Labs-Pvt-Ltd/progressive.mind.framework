package com.progressive.minds.chimera.DataManagement.datalineage.utils;

import java.util.List;

// Represents the entire column lineage structure
public class ColumnLineage {
    private String targetColumn;
    private String targetOrigin;
    private List<SourceColumn> sourceColumns;

    // Constructor
    public ColumnLineage(String targetColumn, String targetOrigin, List<SourceColumn> sourceColumns) {
        this.targetColumn = targetColumn;
        this.targetOrigin = targetOrigin;
        this.sourceColumns = sourceColumns;
    }

    // Getters & Setters
    public String getTargetColumn() {
        return targetColumn;
    }

    public void setTargetColumn(String targetColumn) {
        this.targetColumn = targetColumn;
    }

    public String getTargetOrigin() {
        return targetOrigin;
    }

    public void setTargetOrigin(String targetOrigin) {
        this.targetOrigin = targetOrigin;
    }

    public List<SourceColumn> getSourceColumns() {
        return sourceColumns;
    }

    public void setSourceColumns(List<SourceColumn> sourceColumns) {
        this.sourceColumns = sourceColumns;
    }

    @Override
    public String toString() {
        return "ColumnLineage{" +
                "targetColumn='" + targetColumn + '\'' +
                ", targetOrigin='" + targetOrigin + '\'' +
                ", sourceColumns=" + sourceColumns +
                '}';
    }
}

// Represents the source column details
class SourceColumn {
    private String sourceTable;
    private String sourceColumn;

    // Constructor
    public SourceColumn(String sourceTable, String sourceColumn) {
        this.sourceTable = sourceTable;
        this.sourceColumn = sourceColumn;
    }

    // Getters & Setters
    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public String getSourceColumn() {
        return sourceColumn;
    }

    public void setSourceColumn(String sourceColumn) {
        this.sourceColumn = sourceColumn;
    }

    @Override
    public String toString() {
        return "SourceColumn{" +
                "sourceTable='" + sourceTable + '\'' +
                ", sourceColumn='" + sourceColumn + '\'' +
                '}';
    }
}
