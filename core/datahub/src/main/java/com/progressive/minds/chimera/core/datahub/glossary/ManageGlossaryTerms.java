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

            if (glossaryTerm.glossaryTermName == null || glossaryTerm.glossaryTermName.isEmpty()) {
                return "Glossary term name is required.";
            }
            if (glossaryTerm.documentations == null || glossaryTerm.documentations.isEmpty()) {
                return "Glossary term definition is required.";
            }

            GlossaryTermUrn glossaryNodeUrn = new GlossaryTermUrn(glossaryTerm.glossaryTermName);


            GlossaryTermInfo termInfo = new GlossaryTermInfo()
                    .setId(glossaryNodeUrn.getNameEntity())
                    .setName(glossaryTerm.glossaryTermName)
                    .setDefinition(glossaryTerm.documentations)
                    .setTermSource(glossaryTerm.sourceRef);

            if (glossaryTerm.sourceURL != null && !glossaryTerm.sourceURL.isEmpty()) {
                termInfo.setSourceUrl(new Url(glossaryTerm.sourceURL));
            }

            if (glossaryTerm.glossaryNodeRecord != null && !glossaryTerm.glossaryNodeRecord.name.isEmpty()) {
                // createGlossaryNode()
                termInfo.setParentNode(new GlossaryNodeUrn(glossaryTerm.glossaryNodeRecord.name));
            }


            MetadataChangeProposal glossaryTermProposal = createProposal(String.valueOf(glossaryNodeUrn), GLOSSARY_TERM_ENTITY_NAME,
                    GLOSSARY_TERM_INFO_ASPECT_NAME, "UPSERT", termInfo);

            emitProposal(glossaryTermProposal, "glossaryTermProperties");

            GlossaryRelatedTerms relatedTerms = new GlossaryRelatedTerms();
            if (glossaryTerm.glossaryRelatedTermsRecord != null && !glossaryTerm.glossaryRelatedTermsRecord.isEmpty()) {
                for (GlossaryRelatedTerm glossaryRelatedTerm : glossaryTerm.glossaryRelatedTermsRecord) {
                    if (glossaryRelatedTerm.RelatedTermName != null && glossaryRelatedTerm.RelatedTermName.length > 0) {
                        GlossaryTermUrnArray contains = new GlossaryTermUrnArray();
                        GlossaryTermUrnArray inherits = new GlossaryTermUrnArray();
                        GlossaryTermUrnArray containedBy = new GlossaryTermUrnArray();
                        GlossaryTermUrnArray inheritedBy = new GlossaryTermUrnArray();

                        for (String termName : glossaryRelatedTerm.RelatedTermName) {
                            System.out.println("Related Term: " + termName);
                            if ("INHERITED BY".equalsIgnoreCase(glossaryRelatedTerm.RelationType)) {
                                inheritedBy.add(new GlossaryTermUrn(termName));
                            } else if ("CONTAINED BY".equalsIgnoreCase(glossaryRelatedTerm.RelationType)) {
                                containedBy.add(new GlossaryTermUrn(termName));
                            } else if ("INHERITS".equalsIgnoreCase(glossaryRelatedTerm.RelationType)) {
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
                    GLOSSARY_RELATED_TERM_ASPECT_NAME, "UPSERT", relatedTerms);
            retVal = emitProposal(relatedTermsProposal, GLOSSARY_RELATED_TERM_ASPECT_NAME);
        }
        return retVal;
    }
}
