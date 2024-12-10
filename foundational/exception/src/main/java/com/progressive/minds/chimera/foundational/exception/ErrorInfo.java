package com.progressive.minds.chimera.foundational.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;
import java.util.List;

public class ErrorInfo {
    private List<String> message;
    private Map<String, ErrorSubInfo> subClass;
    private String sqlState;

    @JsonIgnore
    public String getMessageTemplate() {
        return message != null ? String.join("\n", message) : "";
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public Map<String, ErrorSubInfo> getSubClass() {
        return subClass;
    }

    public void setSubClass(Map<String, ErrorSubInfo> subClass) {
        this.subClass = subClass;
    }

    public String getSqlState() {
        return sqlState;
    }

    public void setSqlState(String sqlState) {
        this.sqlState = sqlState;
    }
}
