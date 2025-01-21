/*
package com.progressive.minds.chimera.core.datahub.referances;

import com.linkedin.common.urn.Urn;
import com.linkedin.data.ByteString;
import com.linkedin.mxe.GenericAspect;
import com.linkedin.mxe.MetadataChangeProposal;
import datahub.client.Emitter;
import datahub.client.MetadataWriteResponse;

import java.util.concurrent.Future;

import static com.linkedin.events.metadata.ChangeType.UPSERT;

public class AssociateDataProductWithDomain {
    public static void main(String[] args) {
        try {


            // Define the Data Product URN
            Urn dataProductUrn = Urn.createFromString("urn:li:dataProduct:(urn:li:dataPlatform:snowflake,my_data_product,PROD)");

            // Define the Domain URN
            Urn domainUrn = Urn.createFromString("urn:li:domain:ac347652-724c-44a7-9849-1bf8b3340dad");

            String domainJson = String.format("{\"domains\":\"%s\"}", domainUrn);

            // Create the GenericAspect
            ByteString byteString = ByteString.unsafeWrap(domainJson.getBytes("UTF-8"));
            GenericAspect genericAspect = new GenericAspect();
            genericAspect.setValue(byteString);
            genericAspect.setContentType("application/json");

               // Create the MetadataChangeProposal
            MetadataChangeProposal proposal = new MetadataChangeProposal();
            proposal.setEntityUrn(dataProductUrn); // Set the entity URN
            proposal.setAspectName("domain");     // Set the aspect name
            proposal.setAspect(genericAspect); // Set the aspect value
            // proposal.setSystemMetadata()
            // proposal.setEntityType()
            proposal.setChangeType(UPSERT); // Change type: UPSERT

            Emitter emitter = RestEmitterUtil.getEmitter();
            Future<MetadataWriteResponse> response = emitter.emit(proposal, null);
            String returnCode = response.get().getResponseContent();
            if (returnCode.contains("success"))
            {System.out.println("Domain created successfully!");}
            else
            {System.out.println(returnCode);}

            System.out.println("Data product associated with domain successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/
