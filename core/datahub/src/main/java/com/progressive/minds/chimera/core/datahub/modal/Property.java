package com.progressive.minds.chimera.core.datahub.modal;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Property {

    // Getters and Setters
    @NotNull
    private String name;

    @NotNull
    private String value;

    public Property(){}
    // Constructor
    public Property(String name, String value) {
        this.name = name;
        this.value = value;
    }

}
