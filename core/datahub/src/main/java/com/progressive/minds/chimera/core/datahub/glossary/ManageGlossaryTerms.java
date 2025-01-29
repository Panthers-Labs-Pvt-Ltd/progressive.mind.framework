package com.progressive.minds.chimera.core.datahub.glossary;

import com.linkedin.common.GlossaryTermUrnArray;
import com.linkedin.common.url.Url;
import com.linkedin.common.urn.GlossaryTermUrn;
import com.linkedin.common.urn.GlossaryNodeUrn;

import com.linkedin.glossary.GlossaryRelatedTerms;
import com.linkedin.glossary.GlossaryTermInfo;
import com.linkedin.mxe.MetadataChangeProposal;
import com.progressive.minds.chimera.core.datahub.modal.GlossaryRelatedTerm;
import com.progressive.minds.chimera.core.datahub.modal.GlossaryTerm;
import java.util.List;

import static com.progressive.minds.chimera.core.datahub.Constants.*;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.*;

public class ManageGlossaryTerms {

    public static String createGlossaryTerm(List<GlossaryTerm> glossaryTerms) throws Exception {
        String retVal = "";
        for (GlossaryTerm glossaryTerm : glossaryTerms) {

            if (glossaryTerm.getGlossaryTermName() == null || glossaryTerm.getGlossaryTermName().isEmpty()) {
                return "Glossary term name is required.";
            }
            if (glossaryTerm.getDocumentations() == null || glossaryTerm.getDocumentations().isEmpty()) {
                return "Glossary term definition is required.";
            }

            GlossaryTermUrn glossaryNodeUrn = new GlossaryTermUrn(glossaryTerm.getGlossaryTermName());


            GlossaryTermInfo termInfo = new GlossaryTermInfo()
                    .setId(glossaryNodeUrn.getNameEntity())
                    .setName(glossaryTerm.getGlossaryTermName())
                    .setDefinition(glossaryTerm.getDocumentations())
                    .setTermSource(glossaryTerm.getSourceRef());

            if (glossaryTerm.getSourceURL() != null && !glossaryTerm.getSourceURL().isEmpty()) {
                termInfo.setSourceUrl(new Url(glossaryTerm.getSourceURL()));
            }

            if (glossaryTerm.getGlossaryNodeRecord() != null && !glossaryTerm.getGlossaryNodeRecord().name.isEmpty()) {
                // createGlossaryNode()
                termInfo.setParentNode(new GlossaryNodeUrn(glossaryTerm.getGlossaryNodeRecord().name));
            }


            MetadataChangeProposal glossaryTermProposal = createProposal(String.valueOf(glossaryNodeUrn), GLOSSARY_TERM_ENTITY_NAME,
                    GLOSSARY_TERM_INFO_ASPECT_NAME, ACTION_TYPE, termInfo);

            emitProposal(glossaryTermProposal, "glossaryTermProperties");

            GlossaryRelatedTerms relatedTerms = new GlossaryRelatedTerms();
            if (glossaryTerm.getGlossaryRelatedTermsRecord() != null && !glossaryTerm.getGlossaryRelatedTermsRecord().isEmpty()) {
                for (GlossaryRelatedTerm glossaryRelatedTerm : glossaryTerm.getGlossaryRelatedTermsRecord()) {
                    if (glossaryRelatedTerm.getRelatedTermName() != null && glossaryRelatedTerm.getRelatedTermName().length > 0) {
                        GlossaryTermUrnArray contains = new GlossaryTermUrnArray();
                        GlossaryTermUrnArray inherits = new GlossaryTermUrnArray();
                        GlossaryTermUrnArray containedBy = new GlossaryTermUrnArray();
                        GlossaryTermUrnArray inheritedBy = new GlossaryTermUrnArray();

                        for (String termName : glossaryRelatedTerm.getRelatedTermName()) {
                            if ("INHERITED BY".equalsIgnoreCase(glossaryRelatedTerm.getRelationType())) {
                                inheritedBy.add(new GlossaryTermUrn(termName));
                            } else if ("CONTAINED BY".equalsIgnoreCase(glossaryRelatedTerm.getRelationType())) {
                                containedBy.add(new GlossaryTermUrn(termName));
                            } else if ("INHERITS".equalsIgnoreCase(glossaryRelatedTerm.getRelationType())) {
                                inherits.add(new GlossaryTermUrn(termName));
                            } else {
                                contains.add(new GlossaryTermUrn(termName));
                            }
                        }

                        relatedTerms.setValues(contains);
                        relatedTerms.setIsRelatedTerms(containedBy);
                        relatedTerms.setHasRelatedTerms(inheritedBy);
                        relatedTerms.setRelatedTerms(inherits);
                    }
                }
            }

            MetadataChangeProposal relatedTermsProposal = createProposal(String.valueOf(glossaryNodeUrn), GLOSSARY_TERM_ENTITY_NAME,
                    GLOSSARY_RELATED_TERM_ASPECT_NAME, ACTION_TYPE, relatedTerms);
            retVal = emitProposal(relatedTermsProposal, GLOSSARY_RELATED_TERM_ASPECT_NAME);
        }
        return retVal;
    }
}
