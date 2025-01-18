package com.progressive.minds.chimera.core.datahub.common;


import com.linkedin.common.*;
import com.linkedin.common.urn.CorpuserUrn;
import com.linkedin.common.urn.Urn;
import com.linkedin.data.DataMap;
import com.linkedin.mxe.MetadataChangeProposal;
import datahub.shaded.jackson.databind.ObjectMapper;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static com.linkedin.common.OwnershipSourceType.MANUAL;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.createProposal;
import static com.progressive.minds.chimera.core.datahub.common.genericUtils.emitProposal;

public class AddOwnershipExample {
    public static void main(String[] args) throws Exception {

        Map<String, String> ownersInfo = new HashMap<>();
        ownersInfo.put("manish.kumar.gupta@outlook.com", "Data Creator");
        ownersInfo.put("onlineanuja@gmail.com", "Data owner");

        OwnerArray ownerArray = new OwnerArray();

        ownersInfo.forEach((ownerName, ownershipType) -> {
            Owner owner;
            try {
                owner = new Owner()
                        .setOwner(new CorpuserUrn(ownerName))
                        .setSource(new OwnershipSource().setType(MANUAL))
                        .setTypeUrn(Urn.createFromString("urn:li:ownershipType:__system__business_owner")).setType(OwnershipType.CUSTOM)
                        ;
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            ownerArray.add(owner);
        });

        AuditStamp createdStamp = new AuditStamp()
                .setActor(new CorpuserUrn("datahub"))
                .setTime(Instant.now().toEpochMilli());


        Ownership ownership = new Ownership()
                .setOwners(ownerArray).setLastModified(createdStamp);

        DataMap dataMap = ownership.data();

        // Serialize DataMap to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(dataMap);
        System.out.println("Owner Json" + jsonString);
        MetadataChangeProposal proposal = createProposal(String.valueOf("urn:li:dataProduct:manishdataproduct11"), "dataProduct",
                "ownership", "UPSERT",ownership);
        String retVal= emitProposal(proposal, "ownership");
        System.out.println("Product Created With URN ");
        System.out.println(retVal.toString());
    }
}
