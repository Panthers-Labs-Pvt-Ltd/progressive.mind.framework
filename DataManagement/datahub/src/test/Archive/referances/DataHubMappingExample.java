package com.progressive.minds.chimera.core.datahub.referances;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkedin.common.urn.Urn;
import com.linkedin.data.ByteString;
import com.linkedin.dataset.DatasetProperties;
import com.linkedin.events.metadata.ChangeType;
import com.linkedin.mxe.GenericAspect;
import com.linkedin.mxe.MetadataChangeProposal;
import java.nio.charset.StandardCharsets;

import static com.progressive.minds.chimera.core.datahub.common.genericUtils.emitProposal;

public class DataHubMappingExample {

    // Replace special characters and lowercase the domain or asset name
    private static String replaceSpecialCharsAndLowercase(String input) {
        // Replace special characters and lowercase the name (example)
        return input.replaceAll("[^a-zA-Z0-9]", "_").toLowerCase();
    }

    public static void main(String[] args) {
        try {
            // Example domain and asset names
            String domainName = "Data & Analytics Domain";
            String assetName = "fct_users_created";
            String assetTypeName = "dataset";  // Could be other types like "dataJob", "dashboard", etc.
            //urn:li:dataset:(urn:li:dataPlatform:hive,fct_users_created,PROD)

            // Construct the URN for the domain (urn:li:domain:<domain-name>)
            Urn domainUrn = Urn.createFromString("urn:li:domain:" + replaceSpecialCharsAndLowercase(domainName));

            // Construct the URN for the asset (urn:li:<assetTypeName>:<asset-name>)
            Urn assetsUrn = Urn.createFromString("urn:li:dataset:(urn:li:dataPlatform:hive,fct_users_created,PROD)");
            // String retval = ManageDomain.addEntitiesToDomain(assetsUrn, domainUrn);
            // Convert asset URN to JSON string

            DatasetProperties SP = new DatasetProperties();

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(assetsUrn.toString()); // Convert URN to JSON

            // Wrap the JSON string into a ByteString for storage in the aspect
            ByteString byteString = ByteString.unsafeWrap(jsonString.getBytes(StandardCharsets.UTF_8));

            // Create a Generic Aspect to store the asset URN in the domain aspect
            GenericAspect genericAspect = new GenericAspect();
            genericAspect.setValue(byteString);
            genericAspect.setContentType("application/json");

            // Create a MetadataChangeProposal to associate the asset with the domain
            MetadataChangeProposal proposal = new MetadataChangeProposal();
            proposal.setEntityUrn(domainUrn); // Set the domain URN
            proposal.setAspectName("domain"); // Set the aspect name (specifies that we are modifying the domain aspect)
            proposal.setAspect(genericAspect); // Attach the generic aspect to the proposal
            proposal.setChangeType(ChangeType.UPSERT); // Use UPSERT to add or update the domain
            String retval = emitProposal(proposal, "domain");


            System.out.println("Proposal Created: " + retval);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error mapping asset to domain: " + e.getMessage());
        }
    }
}
