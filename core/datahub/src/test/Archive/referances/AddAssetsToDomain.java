package com.progressive.minds.chimera.core.datahub.referances;

import com.linkedin.common.urn.Urn;
import com.linkedin.data.ByteString;
import com.linkedin.events.metadata.ChangeType;
import com.linkedin.mxe.GenericAspect;
import com.linkedin.mxe.MetadataChangeProposal;
import com.linkedin.domain.DomainProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.progressive.minds.chimera.core.datahub.common.genericUtils.emitProposal;


public class AddAssetsToDomain {
    public static void main(String[] args) {
        try {
            // Initialize the domain URN

            Urn domainUrn = Urn.createFromString("urn:li:domain:manishwebworld");
            Urn chartUrn = Urn.createFromString("urn:li:dataset:(urn:li:dataPlatform:hive,fct_users_created,PROD)");

            // Create and send MetadataChangeProposal for each asset
            //addAssetToDomain(datasetUrn, domainUrn);
             addAssetToDomain(chartUrn, domainUrn);

            System.out.println("Assets added to domain successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addAssetToDomain(Urn assetUrn, Urn domainUrn) throws Exception {
        // Create a DomainProperties object
        DomainProperties domainProperties = new DomainProperties().setName("My Domain");

        // Serialize DomainProperties to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(domainUrn.toString());

        // Convert JSON string to ByteString
        ByteString byteString = ByteString.unsafeWrap(jsonString.getBytes("UTF-8"));

        // Create a GenericAspect
        GenericAspect genericAspect = new GenericAspect();
        genericAspect.setValue(byteString);
        genericAspect.setContentType("application/json");

        // Create MetadataChangeProposal
        MetadataChangeProposal proposal = new MetadataChangeProposal();
        proposal.setEntityUrn(assetUrn); // Set the asset URN
        proposal.setAspectName("domain"); // Aspect name
        proposal.setAspect(genericAspect); // Set the aspect
        proposal.setChangeType(ChangeType.UPSERT); // Change type

        // Emit the proposal using the RestEmitter (assume initialized globally)
        emitProposal(proposal, "domain");
    }

}
