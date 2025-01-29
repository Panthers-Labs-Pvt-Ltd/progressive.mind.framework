package com.progressive.minds.chimera.core.datahub.modal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
//@JsonIgnoreProperties(ignoreUnknown = true)

public class Field {

    public Field(){}

    public Field(String name, String type, String displayName, String description, String fieldCanonicalName,
                 int maxLength, boolean isPartitionKey, boolean isPrimaryKey, boolean isSampleTime, boolean isNullable,
                 List<ForeignKey> foreignKey, List<Tag> tags, List<GlossaryTerm> glossaryTerm) {
        this.name = name;
        this.type = type;
        this.displayName = displayName;
        this.description = description;
        this.fieldCanonicalName = fieldCanonicalName;
        this.maxLength = maxLength;
        this.isPartitionKey = isPartitionKey;
        this.isPrimaryKey = isPrimaryKey;
        this.isSampleTime = isSampleTime;
        this.isNullable = isNullable;
        this.foreignKey = foreignKey;
        this.tags = tags;
        this.glossaryTerm = glossaryTerm;
    }

    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;
    @JsonProperty("displayName")
    private String displayName;
    @JsonProperty("description")
    private String description;
    @JsonProperty("fieldCanonicalName")
    private String fieldCanonicalName;
    @JsonProperty("maxLength")
    private int maxLength;
    @JsonProperty("isPartitionKey")
    private boolean isPartitionKey;
    @JsonProperty("isPrimaryKey")
    private boolean isPrimaryKey;
    @JsonProperty("isSampleTime")
    private boolean isSampleTime;
    @JsonProperty("isNullable")
    private boolean isNullable;
    @JsonProperty("foreignKey")
    private List<ForeignKey> foreignKey;
    @JsonProperty("tags")
    private List<Tag> tags;
    @JsonProperty("glossaryTerm")
    private List<GlossaryTerm> glossaryTerm;
}
