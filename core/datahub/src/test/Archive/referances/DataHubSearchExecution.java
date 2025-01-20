package com.progressive.minds.chimera.core.datahub.referances;


import com.progressive.minds.chimera.core.databaseOps.model.datahub.MetadataAspectV2;
import com.progressive.minds.chimera.core.databaseOps.repository.datahub.MetadataAspectV2Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataHubSearchExecution {
    public static void main(String[] args) {
        System.setProperty("CHIMERA_EXE_ENV", "datahub");
        Map<String, Object> filters = new HashMap<>();
        filters.put("urn","urn:li:dataProduct:chimeradataproduct4");
        List<MetadataAspectV2> returnVal = MetadataAspectV2Repository.getConfig(filters);
        System.out.println(returnVal.size());

        for (MetadataAspectV2 aspect : returnVal) {
            System.out.println("URN: " + aspect.getUrn());
            System.out.println("Aspect: " + aspect.getAspect());
            System.out.println("Version: " + aspect.getVersion());
            System.out.println("Metadata: " + aspect.getMetadata());
            System.out.println("System Metadata: " + aspect.getSystemMetadata());
            System.out.println("Created On: " + aspect.getCreatedOn());
            System.out.println("Created By: " + aspect.getCreatedBy());
            System.out.println("Created For: " + aspect.getCreatedFor());
            System.out.println("-----------------------------------");
        }

    }

}