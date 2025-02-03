/*
package com.progressive.minds.chimera.core.datahub.dataContract;


//import com. linkedin. common. DataPlatformUrn;
import com.linkedin.common.FabricType;
import com. linkedin. common. urn. DataPlatformUrn;
import com.linkedin.common.urn.Urn;
import com.linkedin.datacontract.*;
import com.linkedin.metadata.key.DataContractKey;
import com.linkedin.metadata.snapshot.DatasetSnapshot;
import com.linkedin.metadata.snapshot.Snapshot;
import com.linkedin.metadata.aspect.DatasetAspect;
import com.linkedin.common.urn.DatasetUrn;

import java.net.URISyntaxException;

import static com.progressive.minds.chimera.core.datahub.Constants.ASSERTION_ENTITY_NAME;

public class DataContractExample {

    public static void main(String[] args) throws URISyntaxException {

        DataPlatformUrn platform = new DataPlatformUrn("Snowflake");
        String datasetName = "UserProfile";
        FabricType fabric = FabricType.PROD;

        DatasetUrn datasetUrn = new DatasetUrn(platform, datasetName, fabric);


*/
/*
        // Create a dataset snapshot with the data contract schema
        DatasetSnapshot datasetSnapshot = new DatasetSnapshot()
                .setUrn(datasetUrn)
                .setAspects(new DatasetAspect[]{
                        new DatasetAspect().
                });

*//*



        DataContractKey dataContractKey = new DataContractKey();
        dataContractKey.setId("user-profile-contract");

        SchemaContract schemaContract = new SchemaContract();
        schemaContract.setAssertion(new Urn(""));


        FreshnessContract freshnessContract = new FreshnessContract();
        freshnessContract.setAssertion(new Urn(""));


        DataQualityContract dataQualityContract = new DataQualityContract();
        dataQualityContract.setAssertion(new Urn(""));

        SchemaContractArray schemaContractArray = new SchemaContractArray();
        schemaContractArray.add()
        // Set properties of the Data Contract
        DataContractProperties dataContractProperties = new DataContractProperties();
        dataContractProperties.setRawContract("Version 1 contract for UserProfile data")
                .setEntity(Urn.createFromString("com.example.UserProfile"))
                .setSchema(new SchemaContractArray())
                .setFreshness(new FreshnessContractArray());

        // Create DataContractSnapshot
        DataContractSnapshot dataContractSnapshot = new DataContractSnapshot()
                .setKey(dataContractKey)
                .setProperties(dataContractProperties);

        // Wrap in a snapshot object for ingestion
        Snapshot snapshot = new Snapshot().setDataContractSnapshot(dataContractSnapshot);

        // Wrap in a snapshot and publish
      //  Snapshot snapshot = new Snapshot().setDatasetSnapshot(datasetSnapshot);
        //ASSERTION_ENTITY_NAME
        try {
            client.ingest(snapshot);
            System.out.println("Data contract schema successfully ingested to DataHub!");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to ingest the schema.");
        }
    }
}
*/
