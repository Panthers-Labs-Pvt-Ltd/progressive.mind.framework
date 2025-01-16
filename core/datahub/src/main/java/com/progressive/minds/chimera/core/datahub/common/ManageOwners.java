package com.progressive.minds.chimera.core.datahub.common;

import com.linkedin.common.*;
import com.linkedin.data.ByteString;
import com.linkedin.data.DataMap;
import com.linkedin.mxe.GenericAspect;
import com.linkedin.mxe.MetadataChangeProposal;
import datahub.shaded.jackson.databind.ObjectMapper;
import com.linkedin.common.urn.CorpuserUrn;
import com.linkedin.common.urn.Urn;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.progressive.minds.chimera.core.datahub.common.genericUtils.createProposal;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.emitProposal;

public class ManageOwners {
    public String addOwners(Urn entityUrn, String entityType, String aspectName,String changeType,
                            Map<String, String> ownersInfo) throws IOException, ExecutionException, InterruptedException {
        OwnerArray ownerArray = new OwnerArray();

        // Iterate over the domainOwners map and create Owner objects
        ownersInfo.forEach((ownerName, ownershipType) -> {
            Owner owner = new Owner()
                    .setOwner(new CorpuserUrn(ownerName))
                    .setType(OwnershipType.valueOf(ownershipType)); // Using OwnershipType Enum
            ownerArray.add(owner);
        });
        Ownership ownership = new Ownership()
                .setOwners(ownerArray);

        DataMap dataMap = ownership.data(); // Convert to DataMap

        // Serialize DataMap to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(dataMap);

        // Convert JSON string to ByteString
        ByteString byteString = ByteString.unsafeWrap(jsonString.getBytes(StandardCharsets.UTF_8));
        GenericAspect genericAspect = new GenericAspect();
        genericAspect.setValue(byteString); // Use ByteString
        genericAspect.setContentType("application/json");

        MetadataChangeProposal proposal = createProposal(String.valueOf(entityUrn), entityType,
                aspectName, changeType,genericAspect);

        return emitProposal(proposal, "ownership");
    }
}
