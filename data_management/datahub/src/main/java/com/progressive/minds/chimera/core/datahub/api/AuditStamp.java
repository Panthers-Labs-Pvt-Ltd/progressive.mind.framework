package com.progressive.minds.chimera.core.datahub.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuditStamp {
    @JsonProperty("time")
    private long time;

    @JsonProperty("actor")
    private String actor;

    @JsonProperty("impersonator")
    private String impersonator;

    @JsonProperty("message")
    private String message;

    // Getters and Setters
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getImpersonator() {
        return impersonator;
    }

    public void setImpersonator(String impersonator) {
        this.impersonator = impersonator;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
