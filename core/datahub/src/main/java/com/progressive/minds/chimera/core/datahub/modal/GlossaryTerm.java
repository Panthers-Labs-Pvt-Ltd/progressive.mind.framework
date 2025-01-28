package com.progressive.minds.chimera.core.datahub.modal;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.Null;

public class GlossaryTerm {

    public String glossaryTermName;
    public String documentations;
    public String parentTermName;
    public String sourceRef;
    public String sourceURL;
    public GlossaryNodeGroup  glossaryNodeRecord;
    public List<GlossaryRelatedTerm> glossaryRelatedTermsRecord;
    public Map<String, String> customProperties;

    public GlossaryTerm(){}

    public GlossaryTerm(String glossaryTermName, String documentations){
        this.glossaryTermName = glossaryTermName;
        this.documentations = documentations;
    }

    // Constructor
    public GlossaryTerm(String glossaryTermName, String documentations, @Null String parentTermName,
                               @Null String sourceRef, @Null String sourceURL, @Null GlossaryNodeGroup glossaryNodeRecord,
                               @Null List<GlossaryRelatedTerm> glossaryRelatedTermsRecord,
                               @Null Map<String, String> customProperties) {
        this.glossaryTermName = glossaryTermName;
        this.documentations = documentations;
        this.parentTermName = parentTermName;
        this.sourceRef = sourceRef;
        this.sourceURL = sourceURL;
        this.glossaryNodeRecord = glossaryNodeRecord;
        this.glossaryRelatedTermsRecord = glossaryRelatedTermsRecord;
        this.customProperties = customProperties;
    }

}
