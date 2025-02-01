package com.progressive.minds.chimera.core.datahub.ownership;

import com.linkedin.common.OwnershipType;
import com.progressive.minds.chimera.core.datahub.modal.Owners;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.progressive.minds.chimera.core.datahub.Constants.DATASET_ENTITY_NAME;
import static org.junit.jupiter.api.Assertions.*;

class ManageOwnersTest {

    @Test
    void modifyOwners() throws IOException, ExecutionException, InterruptedException, URISyntaxException {
        Owners owner= new Owners();
        owner.setName("Manish");
        owner.setType(OwnershipType.BUSINESS_OWNER.toString());
        List<Owners> ownersList = List.of(owner);

        ManageOwners.modifyOwners("ADD", "urn:li:dataset:(urn:li:dataPlatform:hive,fct_users_created,PROD)",
                DATASET_ENTITY_NAME, ownersList);
    }
}