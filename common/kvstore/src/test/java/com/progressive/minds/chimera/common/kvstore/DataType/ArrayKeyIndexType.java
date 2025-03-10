package com.progressive.minds.chimera.common.kvstore.DataType;

import com.progressive.minds.chimera.common.kvstore.KVIndex;

import java.util.Arrays;

public class ArrayKeyIndexType {

    @KVIndex
    public int[] key;

    @KVIndex("id")
    public String[] id;

    @Override
    public boolean equals(Object o) {
        if (o instanceof ArrayKeyIndexType other) {
            return Arrays.equals(key, other.key) && Arrays.equals(id, other.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(key) ^ Arrays.hashCode(id);
    }

}
