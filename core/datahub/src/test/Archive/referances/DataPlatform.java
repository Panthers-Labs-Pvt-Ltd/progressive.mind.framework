/*
package com.progressive.minds.chimera.core.datahubutils;

import com.linkedin.common.urn.DataPlatformUrn;
import com.linkedin.common.urn.Urn;
import com.linkedin.data.ByteString;
import com.linkedin.data.DataMap;
import com.linkedin.metadata.key.DataPlatformKey;
import com.linkedin.mxe.GenericAspect;
import com.linkedin.mxe.MetadataChangeProposal;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.progressive.minds.chimera.core.datahubutils.emitter.RestEmitterUtil;
import datahub.client.Emitter;
import datahub.client.MetadataWriteResponse;

import java.util.concurrent.Future;

import static com.linkedin.events.metadata.ChangeType.UPSERT;

public class DataPlatform {
    public static void main(String[] args) {
        try {
            // Define the entity URN
            // Urn datasetUrn = Urn.createFromString("urn:li:dataset:(urn:li:dataPlatform:manish,manishDatasets,PROD)");

            // Define the data platform URN
            DataPlatformUrn platformUrn = new DataPlatformUrn("myDataPlatform");

            // Create the DataPlatformKey aspect
            DataPlatformKey dataPlatformKey = new DataPlatformKey().setPlatformName("myDataPlatform1");


            // Serialize the DataPlatformKey aspect
            DataMap dataMap = dataPlatformKey.data();
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(dataMap);

            // Convert JSON string to ByteString
            ByteString byteString = ByteString.unsafeWrap(jsonString.getBytes("UTF-8"));

            // Create GenericAspect
            GenericAspect genericAspect = new GenericAspect();
            genericAspect.setValue(byteString);
            genericAspect.setContentType("application/json");

            // Create MetadataChangeProposal
            MetadataChangeProposal proposal = new MetadataChangeProposal();
            proposal.setEntityUrn(platformUrn); // Set the dataset URN
            proposal.setAspectName("dataPlatformKey"); // Aspect name
            proposal.setAspect(genericAspect); // Set the aspect
            proposal.setChangeType(UPSERT); // Change type
            Emitter emitter = RestEmitterUtil.getEmitter();

            Future<MetadataWriteResponse> response = emitter.emit(proposal, null);
            String returnCode = response.get().getResponseContent();
            if (returnCode.contains("success"))
            {System.out.println("Domain created successfully!");}
            else
            {System.out.println(returnCode);}

            System.out.println("Proposal " + proposal);

            System.out.println("DataPlatformKey added successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/
