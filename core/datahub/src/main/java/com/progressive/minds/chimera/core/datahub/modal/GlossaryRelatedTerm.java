package com.progressive.minds.chimera.core.datahub.modal;

import javax.validation.constraints.NotNull;

public class GlossaryRelatedTerm {

    // Instance Variables
    public String[] RelatedTermName;
    public String RelationType;
    public GlossaryRelatedTerm() {
    }

    // Constructor
    public GlossaryRelatedTerm(@NotNull String[] RelatedTermName, @NotNull String RelationType) {
        this.RelatedTermName = RelatedTermName;
        this.RelationType = RelationType;

    }
}
