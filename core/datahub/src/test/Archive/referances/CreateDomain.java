package com.progressive.minds.chimera.core.datahub.referances;

import com.linkedin.common.*;
import com.linkedin.common.urn.Urn;
import com.linkedin.data.template.StringMap;
import com.linkedin.events.metadata.ChangeType;
import com.linkedin.mxe.GenericAspect;
import com.linkedin.mxe.MetadataChangeProposal;
import com.linkedin.domain.DomainProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkedin.data.DataMap;
import com.linkedin.data.ByteString;
import com.linkedin.common.urn.CorpuserUrn;

import java.time.Instant;

import static com.progressive.minds.chimera.core.datahub.common.genericUtils.emitProposal;


public class CreateDomain {
    public static void main(String[] args){
        try
        {
            Urn domainUrn = Urn.createFromString("urn:li:domain:manishwebworldChild");
            Urn domainUrnChild = Urn.createFromString("urn:li:domain:manishwebworldsub");
            Urn ParentDomainUrn = Urn.createFromString("urn:li:domain:manishwebworld");
            // Define creation metadata
            AuditStamp createdStamp = new AuditStamp()
                    .setActor(new CorpuserUrn("data_creator"))
                    .setTime(Instant.now().toEpochMilli());    // Current timestamp in milliseconds

                 // Add custom properties

            StringMap customProperties = new StringMap();
            customProperties.put("owner", "Data Team");
            customProperties.put("region", "US-East");
            customProperties.put("compliance", "GDPR");

            // Create DomainProperties Aspect
            DomainProperties domainProperties = new DomainProperties()
                     .setCustomProperties(customProperties)
                    .setName("Manish Web World -DomainTest")  // Domain name
                    .setCreated(createdStamp)
                    .setParentDomain(domainUrnChild)
                    .setDescription("This is a My Child Domain."); // Domain description

            // Convert the aspect DataMap to JSON and then to ByteString
            DataMap dataMap = domainProperties.data(); // Extract the DataMap from the aspect
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(dataMap); // Serialize DataMap to JSON
            ByteString byteString = ByteString.copyAvroString(jsonString, true); // Convert JSON string to ByteString

            // Create GenericAspect
            GenericAspect genericAspect = new GenericAspect();
            genericAspect.setValue(byteString); // Set the ByteString value
            genericAspect.setContentType("application/json"); // Specify the content type

            MetadataChangeProposal proposal = new MetadataChangeProposal();
            proposal.setEntityUrn(domainUrn); // Set the domain URN
            proposal.setEntityType("domain"); // Entity type is "domain"
            proposal.setAspectName("domainProperties"); // Aspect name
            proposal.setAspect(genericAspect); // Set the GenericAspect
            proposal.setChangeType(ChangeType.UPSERT); // Change type: UPSERT
            emitProposal(proposal, "domain");
  /*        Future<MetadataWriteResponse> response = getEmitter().emit(proposal, null);
            String returnCode = response.get().getResponseContent();
            if (returnCode.contains("success"))
            {System.out.println("Domain created successfully!");}
            else
            {System.out.println(returnCode);}*/


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}