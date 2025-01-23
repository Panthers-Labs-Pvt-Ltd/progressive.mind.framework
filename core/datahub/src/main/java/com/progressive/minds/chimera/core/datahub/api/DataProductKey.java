package com.progressive.minds.chimera.core.datahub.api;

public class DataProductKey {
    private DataProductKeyValue value;

    // Getters and Setters
    public DataProductKeyValue getValue() {
        return value;
    }

    public void setValue(DataProductKeyValue value) {
        this.value = value;
    }

    public static class DataProductKeyValue {
        private String id;

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
