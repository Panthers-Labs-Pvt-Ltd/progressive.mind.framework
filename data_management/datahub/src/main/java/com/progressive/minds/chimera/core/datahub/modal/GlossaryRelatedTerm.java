package com.progressive.minds.chimera.core.datahub.modal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlossaryRelatedTerm {

    private String[] relatedTermName;  // Renamed to camelCase
    private String relationType;       // Renamed to camelCase

    public GlossaryRelatedTerm(){}

    // Constructor with Lombok's generated NoArgsConstructor
    public GlossaryRelatedTerm(String[] relatedTermName, String relationType) {
        this.relatedTermName = relatedTermName;
        this.relationType = relationType;
    }
}
