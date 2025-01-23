package com.progressive.minds.chimera.core.datahub.common;

import com.linkedin.common.urn.Urn;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


class ManageOwnersTest {

    @Test
    void createDataProductTest() throws IOException, URISyntaxException, ExecutionException, InterruptedException {

        ManageOwners Ownerss = new ManageOwners();

        Map<String, String> owner = new HashMap<>();
        owner.put("John Doe", "Data Creator");
        owner.put("Manu", "Data owner");

        String retval = ManageOwners.addOwners(Urn.createFromString("urn:li:dataProduct:manishdataproduct11"),
                "dataProduct", "ownership","UPSERT",
                owner);

        System.out.println("Product Created With URN ");
        System.out.println(retval.toString());
    }
}