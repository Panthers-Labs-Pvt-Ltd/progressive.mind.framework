package com.progressive.minds.chimera.core.datahub.common;

import com.linkedin.common.urn.Urn;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class ManageGlobalTagsTest {
    @Test
    void TagTest() throws Exception {

        String[] tagNames = {"Verified","Unauth"};

       String retval= ManageGlobalTags.addTags(Urn.createFromString("urn:li:dataProduct:manishdataproduct11"),
                "dataProduct", "UPSERT", tagNames);

        System.out.println(retval.toString());
    }
}