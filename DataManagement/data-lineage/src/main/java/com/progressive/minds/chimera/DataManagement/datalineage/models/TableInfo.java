package com.progressive.minds.chimera.DataManagement.datalineage.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
class TableInfo {
    public List<String> inTables;
    public List<String> outTables;

    public TableInfo() {
        this.inTables = new ArrayList<>();
        this.outTables = new ArrayList<>();
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
class ColumnLineage {
    public Descendant descendant;
    public List<Lineage> lineage;

    public ColumnLineage() {
        this.lineage = new ArrayList<>();
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
class Descendant {
    public String origin;
    public String name;
}

@JsonInclude(JsonInclude.Include.NON_NULL)
class Lineage {
    public String origin;
    public String name;
    public String transformationType;
    public String transformationDesc;
}

@JsonInclude(JsonInclude.Include.NON_NULL)
class PlanOutput {
    public TableInfo tableInfo;
    public List<ColumnLineage> columnLineages;

    public PlanOutput() {
        this.tableInfo = new TableInfo();
        this.columnLineages = new ArrayList<>();
    }
}
