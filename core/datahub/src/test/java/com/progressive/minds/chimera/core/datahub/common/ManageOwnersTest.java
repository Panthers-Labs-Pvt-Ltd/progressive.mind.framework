package com.progressive.minds.chimera.core.datahub.common;

import com.linkedin.common.urn.Urn;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import com.progressive.minds.chimera.core.datahub.modal.Owners;

class ManageOwnersTest {

    @Test
    void createDataProductTest() throws IOException, URISyntaxException, ExecutionException, InterruptedException {
        List<Owners> owners = new ArrayList<>();

        Owners owner1 = new Owners();
        owner1.name = "Alice";
        owner1.type = "CUSTOM"; // valid type
        owners.add(owner1);

        Owners owner2 = new Owners();
        owner2.name = "Bob";
        owner2.type = "DEVELOPER"; // valid type
        owners.add(owner2);

        Owners owner3 = new Owners();
        owner3.name = "Charlie";
        owner3.type = "UNKNOWN_TYPE"; // invalid type
        owners.add(owner3);

        String retval = ManageOwners.addOwners(Urn.createFromString("urn:li:dataProduct:manishdataproduct11"),
                "dataProduct", "ownership","UPSERT",
                owners);

        System.out.println("Product Created With URN ");
        System.out.println(retval.toString());
    }
}