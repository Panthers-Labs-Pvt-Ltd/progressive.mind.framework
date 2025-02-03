package com.progressive.minds.chimera.core.datahub.tag;

import com.linkedin.common.*;
import com.linkedin.common.urn.Urn;
import com.linkedin.mxe.MetadataChangeProposal;
import com.linkedin.common.urn.TagUrn;

import java.util.ArrayList;

import static com.progressive.minds.chimera.core.datahub.Constants.GLOBAL_TAGS_ASPECT_NAME;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.*;

public class ManageGlobalTags {
    public static String addTags(Urn entityUrn, String entityType, String changeType, String[] tagNames)
            throws Exception {
        TagAssociationArray tagAssociationArray = new TagAssociationArray();
        //MetadataAttribution metadataAttribution = new MetadataAttribution().setActor("").setSource("").setSourceDetail();
        for (String tagName : tagNames) {
            TagUrn tagUrn = new TagUrn(tagName); // Create a TagUrn for each tag name
            TagAssociation tagAssociation = new TagAssociation().setTag(tagUrn).setContext("");//.setAttribution(); // Create a TagAssociation
            tagAssociationArray.add(tagAssociation); // Add the TagAssociation to the array
        }
        GlobalTags globalTags = new GlobalTags().setTags(tagAssociationArray);
        MetadataChangeProposal proposal = createProposal(String.valueOf(entityUrn), entityType,
                GLOBAL_TAGS_ASPECT_NAME, changeType,globalTags);
        return emitProposal(proposal, entityType);
    }

    public static String addTags(Urn entityUrn, String entityType, String changeType, ArrayList<String> tagNames, String Context)
            throws Exception {
        TagAssociationArray tagAssociationArray = new TagAssociationArray();
        for (String tagName : tagNames) {
            TagUrn tagUrn = new TagUrn(tagName);
            TagAssociation tagAssociation = new TagAssociation().setTag(tagUrn).setContext(Context);//.setAttribution(); // Create a TagAssociation
            tagAssociationArray.add(tagAssociation);
        }
        GlobalTags globalTags = new GlobalTags().setTags(tagAssociationArray);
        MetadataChangeProposal proposal = createProposal(String.valueOf(entityUrn), entityType,
                GLOBAL_TAGS_ASPECT_NAME, changeType,globalTags);
        return emitProposal(proposal, entityType);
    }
}
