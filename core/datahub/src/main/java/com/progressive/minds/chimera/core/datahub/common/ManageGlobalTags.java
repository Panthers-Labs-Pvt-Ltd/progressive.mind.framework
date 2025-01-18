package com.progressive.minds.chimera.core.datahub.common;

import com.linkedin.common.*;
import com.linkedin.common.urn.Urn;
import com.linkedin.mxe.MetadataChangeProposal;
import com.linkedin.common.urn.TagUrn;

import static com.progressive.minds.chimera.core.datahub.common.genericUtils.*;

public class ManageGlobalTags {
    public static String addTags(Urn entityUrn, String entityType, String changeType, String[] tagNames)
            throws Exception {
        TagAssociationArray tagAssociationArray = new TagAssociationArray();

        for (String tagName : tagNames) {
            TagUrn tagUrn = new TagUrn(tagName); // Create a TagUrn for each tag name
            TagAssociation tagAssociation = new TagAssociation().setTag(tagUrn); // Create a TagAssociation
            tagAssociationArray.add(tagAssociation); // Add the TagAssociation to the array
        }
        GlobalTags globalTags = new GlobalTags().setTags(tagAssociationArray);
        MetadataChangeProposal proposal = createProposal(String.valueOf(entityUrn), entityType,
                "globalTags", changeType,globalTags);
        return emitProposal(proposal, "ownership");
    }
}
