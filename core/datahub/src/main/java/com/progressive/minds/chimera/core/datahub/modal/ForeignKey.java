package com.progressive.minds.chimera.core.datahub.modal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
public class ForeignKey {

    private String datasetName;
    private String datasetPlatform;
    private String origin;
    private String foreignKeyName; // Renamed to camelCase
    private String foreignKeyColumn; // Renamed to camelCase
    private boolean logicalKey; // Renamed to camelCase

    public ForeignKey(){}

    public ForeignKey(String datasetName, String datasetPlatform, String origin, String foreignKeyName,
                      String foreignKeyColumn, boolean logicalKey) {
        this.datasetName = datasetName;
        this.datasetPlatform = datasetPlatform;
        this.origin = origin;
        this.foreignKeyName = foreignKeyName;
        this.foreignKeyColumn = foreignKeyColumn;
        this.logicalKey = logicalKey;
    }
}
