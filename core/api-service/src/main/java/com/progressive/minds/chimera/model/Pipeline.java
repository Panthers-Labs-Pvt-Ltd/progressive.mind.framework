package com.progressive.minds.chimera.model;

import java.time.LocalDateTime;

public class Pipeline {
    private int id;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String frequency;
    private String schedule;

    public Pipeline() {
    }

    public Pipeline(int id, String name, LocalDateTime createdDate, LocalDateTime lastModifiedDate, String frequency, String schedule) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.frequency = frequency;
        this.schedule = schedule;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
