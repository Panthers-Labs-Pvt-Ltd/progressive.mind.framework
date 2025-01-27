package com.progressive.minds.chimera.core.datahub.modal;

import javax.validation.constraints.NotNull;

public class Tag {
    @NotNull
    public String name;
    @NotNull
    public String value;
    public boolean isInternal;
}
