package com.progressive.minds.chimera.core.datahub.glossary;

import com.progressive.minds.chimera.core.datahub.modal.GlossaryNodeGroup;
import com.progressive.minds.chimera.core.datahub.modal.GlossaryTerm;
import org.junit.jupiter.api.Test;

import java.util.List;

class ManageGlossaryTermsTest {

    @Test
    void createGlossaryTerm() throws Exception {
        GlossaryTerm Term1 = new GlossaryTerm();

        GlossaryNodeGroup glossaryNodeRecord = new GlossaryNodeGroup();
        glossaryNodeRecord.name = "PantherLabsTermGroup";
        glossaryNodeRecord.definition = "My Definitions";


        Term1.setGlossaryTermName("SampleTermName");
        Term1.setDocumentations("This is for SampleTermName");
        Term1.setSourceRef("Manual");
        Term1.setSourceURL("http://manishwebworld.com");
        Term1.setGlossaryNodeRecord(glossaryNodeRecord);


        ManageGlossaryTerms manageGlossaryTerms = new ManageGlossaryTerms();
        List<GlossaryTerm> term = List.of(Term1);
        String result = manageGlossaryTerms.createGlossaryTerm(term);
        System.out.println(result);

    }
}