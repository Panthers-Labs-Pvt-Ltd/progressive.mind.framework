package com.progressive.minds.chimera.core.datahub.common;

import com.linkedin.common.Status;
import com.linkedin.common.url.Url;
import com.linkedin.common.urn.Urn;
import com.linkedin.glossary.GlossaryTermInfo;
import com.linkedin.mxe.GenericAspect;
import com.linkedin.mxe.MetadataChangeProposal;

import static com.progressive.minds.chimera.core.datahub.common.genericUtils.*;
import com.linkedin.glossary.GlossaryNodeInfo;
import com.linkedin.glossary.GlossaryRelatedTerms;

import java.net.URL;


public class ManageGlossaryTermsGroups {
    public record GlossaryTerms (String name, String definition, String parentTermName,
                                 String sourceRef, String sourceURL) {};

    public String createGlossaryTerm(GlossaryTerms glossaryTerms) throws Exception {

        GlossaryTermInfo termInfo = new GlossaryTermInfo()
                .setId(replaceSpecialCharsAndLowercase(glossaryTerms.name()))
                .setName(glossaryTerms.name())
                .setDefinition(glossaryTerms.definition())
                .setSourceRef(glossaryTerms.sourceRef());

        /*if (glossaryTerms.parentTermName() != null) {
            termInfo.setParentNode(glossaryTerms.parentTermName());
        }*/

        if (glossaryTerms.sourceURL() != null) {
            termInfo.setSourceUrl(new Url(glossaryTerms.sourceURL()));
        }



   /*     GlossaryNodeInfo gni = new GlossaryNodeInfo();
        gni.setId();
        gni.setDefinition();
        gni.setCustomProperties();
        gni.setParentNode();

        GlossaryRelatedTerms grt = new GlossaryRelatedTerms();
        grt.setHasRelatedTerms();
        grt.setIsRelatedTerms();
        grt.setValues();
*/
        Status removedStatus = new Status().setRemoved(isRemoved);

        GenericAspect genericAspect = serializeAspect(removedStatus);
        MetadataChangeProposal proposal = createProposal(String.valueOf(entityUrn), entityType,
                "status", changeType,genericAspect);
        return emitProposal(proposal, "status");
    }
}
