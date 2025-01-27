package com.progressive.minds.chimera.core.datahub.modal;

import javax.validation.constraints.NotNull;

public class Property {
    @NotNull
    public String name;
    @NotNull
    public String value;
}
