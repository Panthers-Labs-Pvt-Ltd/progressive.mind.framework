package com.progressive.minds.chimera.core.datahub.modal;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class GlossaryTerm {

    public GlossaryTerm(){}

    public GlossaryTerm(String glossaryTermName, String documentations, String parentTermName, String sourceRef,
                        String sourceURL, GlossaryNodeGroup glossaryNodeRecord, List<GlossaryRelatedTerm>
                                glossaryRelatedTermsRecord, Map<String, String> customProperties) {
        this.glossaryTermName = glossaryTermName;
        this.documentations = documentations;
        this.parentTermName = parentTermName;
        this.sourceRef = sourceRef;
        this.sourceURL = sourceURL;
        this.glossaryNodeRecord = glossaryNodeRecord;
        this.glossaryRelatedTermsRecord = glossaryRelatedTermsRecord;
        this.customProperties = customProperties;
    }

    private String glossaryTermName;
    private String documentations;
    private String parentTermName;
    private String sourceRef;
    private String sourceURL;
    private GlossaryNodeGroup glossaryNodeRecord;
    private List<GlossaryRelatedTerm> glossaryRelatedTermsRecord;
    private Map<String, String> customProperties;

}
