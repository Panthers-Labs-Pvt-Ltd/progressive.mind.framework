package com.progressive.minds.chimera.common.kvstore.DataType;

import com.progressive.minds.chimera.common.kvstore.KVIndex;

public class CustomType1 {

    @KVIndex
    public String key;

    @KVIndex("id")
    public String id;

    @KVIndex(value = "name", copy = true)
    public String name;

    @KVIndex("int")
    public int num;

    @KVIndex(value = "child", parent = "id")
    public String child;

    @Override
    public boolean equals(Object o) {
        if (o instanceof CustomType1 other) {
            return id.equals(other.id) && name.equals(other.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "CustomType1[key=" + key + ",id=" + id + ",name=" + name + ",num=" + num;
    }

}
