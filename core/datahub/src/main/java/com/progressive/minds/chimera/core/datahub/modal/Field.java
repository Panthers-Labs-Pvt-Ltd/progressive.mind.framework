package com.progressive.minds.chimera.core.datahub.modal;


import java.util.List;

public class Field {
    public String name;
    public String type;
    public String displayName;
    public String description;
    public String fieldCanonicalName;
    public int maxLength;
    public boolean isPartitionKey;
    public boolean isPrimaryKey;
    public boolean isSampleTime;
    public boolean isNullable;
    public List<ForeignKey> foreignKey;
    public List<Tag> tags;
    public List<GlossaryTerm> glossaryTerm;
}
