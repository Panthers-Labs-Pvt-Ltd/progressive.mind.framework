package com.progressive.minds.chimera.common.kvstore.DataType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.progressive.minds.chimera.common.kvstore.KVIndex;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomType2 {

    @KVIndex(parent = "parentId")
    public String key;

    @KVIndex("id")
    public String id;

    @KVIndex("parentId")
    public String parentId;

    @Override
    public boolean equals(Object o) {
        if (o instanceof CustomType2 other) {
            return id.equals(other.id) && parentId.equals(other.parentId);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode() ^ parentId.hashCode();
    }

    @Override
    public String toString() {
        return "CustomType2[key=" + key + ",id=" + id + ",parentId=" + parentId;
    }

}
