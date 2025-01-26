/*
package com.progressive.minds.chimera.core.datahub;

import com.linkedin.common.urn.DatasetUrn;
import com.linkedin.common.urn.Urn;
import com.linkedin.common.urn.CorpuserUrn;
import com.linkedin.metadata.client.JavaEntityClient;
import com.linkedin.metadata.key.DatasetKey;
import com.linkedin.metadata.query.Filter;
import com.linkedin.metadata.query.IndexCriterion;
import com.linkedin.metadata.query.IndexFilter;
import com.linkedin.metadata.snapshot.Snapshot;
import com.linkedin.metadata.snapshot.DatasetSnapshot;
import com.linkedin.common.Ownership;
import com.linkedin.common.Owner;
import com.linkedin.common.OwnershipType;
import com.linkedin.common.urn.CorpuserUrn;

import java.util.List;
import java.util.stream.Collectors;

public class RemoveOwnerExample {
    static String DATAHUB_URL = "http://localhost:8080";
    static String DATAHUB_AUTH_TOKEN = "";
    public static void main(String[] args) throws Exception {
        // Initialize the JavaEntityClient
        JavaEntityClient client = JavaEntityClientFactory.create(
                DATAHUB_URL,
                DATAHUB_AUTH_TOKEN// optional if security is enabled
        );

        urn:li:dataset:(urn:li:dataPlatform:hive,SampleHiveDataset,PROD)

        // Specify the dataset URN
        DatasetUrn datasetUrn = new DatasetUrn("urn:li:dataPlatform:hive", "SampleHiveDataset", "PROD");

        // Fetch the current ownership aspect
        Ownership ownership = client.getAspect(datasetUrn, "ownership", Ownership.class);

        if (ownership == null || ownership.getOwners() == null || ownership.getOwners().isEmpty()) {
            System.out.println("No owners found for the dataset.");
            return;
        }

        // Remove the specific owner
        String ownerToRemove = "urn:li:corpuser:owner_to_remove";
        List<Owner> updatedOwners = ownership.getOwners().stream()
                .filter(owner -> !owner.getOwner().toString().equals(ownerToRemove))
                .collect(Collectors.toList());

        ownership.setOwners(updatedOwners);

        // Prepare MetadataChangeProposal to update the ownership aspect
        MetadataChangeProposal proposal = new MetadataChangeProposal();
        proposal.setEntityUrn(datasetUrn);
        proposal.setEntityType("dataset");
        proposal.setAspectName("ownership");
        proposal.setChangeType(ChangeType.UPSERT);
        proposal.setAspect(ownership);

        // Send the proposal
        client.ingestProposal(proposal);
        System.out.println("Owner removed successfully.");
    }
}
*/
