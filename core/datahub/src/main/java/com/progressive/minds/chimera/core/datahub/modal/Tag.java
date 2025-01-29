package com.progressive.minds.chimera.core.datahub.modal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class Tag {

    @NotNull
    private String name;

    public Tag(){}

    public Tag(String value, String name) {
        this.value = value;
        this.name = name;
    }

    @NotNull
    private String value;

    private boolean isInternal;
}
