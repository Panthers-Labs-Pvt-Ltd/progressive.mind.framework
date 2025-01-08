package com.progressive.minds.chimera.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Pipeline implements DbEntity{
    private int id;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String frequency;
    private String schedule;
}
