package com.progressive.minds.chimera.core.datahub.common;

import com.linkedin.common.*;
import com.linkedin.common.urn.Urn;
import com.linkedin.data.ByteString;
import com.linkedin.mxe.GenericAspect;
import com.linkedin.mxe.MetadataChangeProposal;
import com.linkedin.common.urn.TagUrn;

import static com.progressive.minds.chimera.core.datahub.common.genericUtils.*;

public class ManageGlobalTags {
    public String addTags(Urn entityUrn, String entityType, String changeType,String[] tagNames)
            throws Exception {
        TagAssociationArray tagAssociationArray = new TagAssociationArray();

        for (String tagName : tagNames) {
            TagUrn tagUrn = new TagUrn(tagName); // Create a TagUrn for each tag name
            TagAssociation tagAssociation = new TagAssociation().setTag(tagUrn); // Create a TagAssociation
            tagAssociationArray.add(tagAssociation); // Add the TagAssociation to the array
        }
        GlobalTags globalTags = new GlobalTags().setTags(tagAssociationArray);

        // Convert GlobalTags aspect to GenericAspect
        GenericAspect genericAspect = new GenericAspect();
        genericAspect.setValue(ByteString.copyAvroString(globalTags.data().toString(), true));
        genericAspect.setContentType("application/json");

        GenericAspect globalTagsAspect = serializeAspect(globalTags);

        MetadataChangeProposal proposal = createProposal(String.valueOf(entityUrn), entityType,
                "globalTags", changeType,globalTagsAspect);
        return emitProposal(proposal, "ownership");
    }
}
