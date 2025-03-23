package com.progressive.minds.chimera.core.datahub.api;

import java.util.List;


public class Ownership {
    private OwnershipValue value;

    // Getters and Setters
    public OwnershipValue getValue() {
        return value;
    }

    public void setValue(OwnershipValue value) {
        this.value = value;
    }

    public static class OwnershipValue {
        private List<Owner> owners;
        private LastModified lastModified;

        // Getters and Setters
        public List<Owner> getOwners() {
            return owners;
        }

        public void setOwners(List<Owner> owners) {
            this.owners = owners;
        }

        public LastModified getLastModified() {
            return lastModified;
        }

        public void setLastModified(LastModified lastModified) {
            this.lastModified = lastModified;
        }
    }

    public static class Owner {
        private String owner;
        private String typeUrn;
        private Source source;
        private String type;

        // Getters and Setters
        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getTypeUrn() {
            return typeUrn;
        }

        public void setTypeUrn(String typeUrn) {
            this.typeUrn = typeUrn;
        }

        public Source getSource() {
            return source;
        }

        public void setSource(Source source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class Source {
        private String type;

        // Getters and Setters
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class LastModified {
        private String actor;
        private long time;

        // Getters and Setters
        public String getActor() {
            return actor;
        }

        public void setActor(String actor) {
            this.actor = actor;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }
}