package com.progressive.minds.chimera.foundational.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class ErrorSubInfo {
    private List<String> message;

    @JsonIgnore
    public String getMessageTemplate() {
        return (message != null) ? String.join("\n", message) : "";
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }
}
