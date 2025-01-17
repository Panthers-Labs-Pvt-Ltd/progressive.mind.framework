package com.progressive.minds.chimera.core.datahub.common;

// import com.linkedin.common.GlossaryNodeUrn;
import com.linkedin.common.Status;
import com.linkedin.common.url.Url;
import com.linkedin.common.urn.Urn;
import com.linkedin.data.template.StringMap;
import com.linkedin.glossary.GlossaryTermInfo;
import com.linkedin.mxe.GenericAspect;
import com.linkedin.mxe.MetadataChangeProposal;

import static com.progressive.minds.chimera.core.datahub.common.genericUtils.*;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.replaceSpecialCharsAndLowercase;

import com.linkedin.glossary.GlossaryNodeInfo;
import com.linkedin.glossary.GlossaryRelatedTerms;

import com.linkedin.common.urn.GlossaryNodeUrn;

import javax.validation.constraints.Null;
import java.net.URL;
import java.util.Map;


public class ManageGlossaryTermsGroups {

    public record GlossaryNodeRecord(String name, String definition, String parentTermName,
                                String sourceRef, String sourceURL, Map<String, String> customProperties) {    }

    public record GlossaryRelatedTermsRecord(String name, String definition, String parentTermName,
                                String sourceRef, String sourceURL, Map<String, String> customProperties) {    }

    public record GlossaryTermsRecord(String name, String definition, String parentTermName,
                                      String sourceRef, String sourceURL,GlossaryNodeRecord glossaryNodeRecord,
                                      GlossaryRelatedTermsRecord glossaryRelatedTermsRecord,
                                      Map<String, String> customProperties) {    }


    public String createGlossaryTerm(GlossaryTermsRecord glossaryTerms) throws Exception {

        GlossaryTermInfo termInfo = new GlossaryTermInfo()
                .setId(replaceSpecialCharsAndLowercase(glossaryTerms.name()))
                .setName(glossaryTerms.name())
                .setDefinition(glossaryTerms.definition())
                .setSourceRef(glossaryTerms.sourceRef());

        if (glossaryTerms.parentTermName() != null) {
            // createGlossaryNode()
            termInfo.setParentNode(GlossaryNodeUrn.createFromString(glossaryTerms.parentTermName()));
        }

        if (glossaryTerms.sourceURL() != null) {
            termInfo.setSourceUrl(new Url(glossaryTerms.sourceURL()));
        }


        GlossaryNodeInfo gni = new GlossaryNodeInfo();
        gni.setId();
        gni.setDefinition();
        gni.setCustomProperties();
        gni.setParentNode();

        GlossaryRelatedTerms grt = new GlossaryRelatedTerms();
        grt.setHasRelatedTerms();
        grt.setIsRelatedTerms();
        grt.setValues();

        Status removedStatus = new Status().setRemoved(isRemoved);

        GenericAspect genericAspect = serializeAspect(removedStatus);
        MetadataChangeProposal proposal = createProposal(String.valueOf(entityUrn), entityType,
                "status", changeType, genericAspect);
        return emitProposal(proposal, "status");
    }

    public String createGlossaryNode(GlossaryNodeRecord glossaryNodeRecord, String name,String definition,
                                     @Null String parentNode, @Null Map<String, String> customProperties )
            throws Exception {

        StringMap MapCustomProperties = new StringMap();


        GlossaryNodeInfo gni = new GlossaryNodeInfo();
        gni.setId(replaceSpecialCharsAndLowercase(name));
        gni.setName(name);
        gni.setDefinition(definition);

        if (customProperties.size() > 0) {
            MapCustomProperties.putAll(customProperties);
            gni.setCustomProperties(MapCustomProperties);
        }
        if (parentNode  != null) {
          /*
            GlossaryNodeUrn currentNodeUrn = new GlossaryNodeUrn();
            GlossaryNodeUrn parentNodeUrn = new GlossaryNodeUrn();
            GlossaryNodeInfo gni = new GlossaryNodeInfo();
            gni.setParentNode(parentNodeUrn);  // Make sure the parent node is set here
            */
            GlossaryNodeUrn nodeUrn = new GlossaryNodeUrn(parentNode);
            gni.setParentNode(nodeUrn);
        }
    }
}