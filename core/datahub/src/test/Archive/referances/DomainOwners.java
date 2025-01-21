package com.progressive.minds.chimera.core.datahub.referances;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkedin.common.Owner;
import com.linkedin.common.OwnerArray;
import com.linkedin.common.Ownership;
import com.linkedin.common.OwnershipType;
import com.linkedin.common.urn.CorpuserUrn;
import com.linkedin.common.urn.Urn;
import com.linkedin.data.ByteString;
import com.linkedin.data.DataMap;
import com.linkedin.mxe.GenericAspect;
import com.linkedin.mxe.MetadataChangeProposal;

import static com.linkedin.events.metadata.ChangeType.UPSERT;

public class DomainOwners {

    public static void main(String[] args) {
        try {
            // Define the domain URN
            Urn domainUrn = Urn.createFromString("urn:li:domain:manishwebworld");

            // Create Owner objects
            Owner owner1 = new Owner()
                    .setOwner(new CorpuserUrn("data_owner1")) // Owner URN
                    .setType(OwnershipType.DATAOWNER);       // Ownership type

            Owner owner2 = new Owner()
                    .setOwner(new CorpuserUrn("data_steward")) // Owner URN
                    .setType(OwnershipType.DATA_STEWARD);      // Ownership type

            // Create an OwnerArray and add the owners
            OwnerArray ownerArray = new OwnerArray();
            ownerArray.add(owner1);
            ownerArray.add(owner2);

            // Create the Ownership aspect and set the owners
            Ownership ownership = new Ownership()
                    .setOwners(ownerArray); // Set the OwnerArray

            DataMap dataMap = ownership.data(); // Convert to DataMap

            // Serialize DataMap to JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(dataMap);

            // Convert JSON string to ByteString
            ByteString byteString = ByteString.unsafeWrap(jsonString.getBytes("UTF-8"));


            GenericAspect genericAspect = new GenericAspect();
            genericAspect.setValue(byteString); // Use ByteString
            genericAspect.setContentType("application/json");



            // Create MetadataChangeProposal
            MetadataChangeProposal proposal = new MetadataChangeProposal();
            proposal.setEntityUrn(domainUrn); // Set the domain URN
            proposal.setEntityType("domain"); // Entity type
            proposal.setAspectName("ownership"); // Aspect name
            proposal.setAspect(genericAspect); // Set the aspect
            proposal.setChangeType(UPSERT); // Change type

            /*Emitter emitter = RestEmitterUtil.getEmitter();
            Future<MetadataWriteResponse> response = emitter.emit(proposal, null);
            String returnCode = response.get().getResponseContent();
            if (returnCode.contains("success"))
            {System.out.println("Domain Owners created successfully!");}
            else
            {System.out.println(returnCode);}
*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
