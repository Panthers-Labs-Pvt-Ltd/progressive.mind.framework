package com.progressive.minds.chimera.core.datahub.common;

import com.linkedin.common.*;
import com.linkedin.common.urn.Urn;
import com.linkedin.mxe.MetadataChangeProposal;
import com.linkedin.common.urn.TagUrn;
import com.linkedin.tag.TagProperties;
import datahub.event.MetadataChangeProposalWrapper;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.linkedin.events.metadata.ChangeType.UPSERT;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.*;

public class ManageTags {
        public static String createTags(String tagName, String tagDescription) throws IOException,
            ExecutionException, InterruptedException {
        String entityUrn = "urn:li:tag:" + replaceSpecialCharsAndLowercase(tagName);
        TagProperties tagProperties =
                new TagProperties()
                        .setName(tagName)
                        .setDescription(tagDescription);

        MetadataChangeProposal proposal = createProposal(entityUrn, "tag",
                "tagProperties", "UPSERT", tagProperties);
        return emitProposal(proposal, "tag");

    }
}
