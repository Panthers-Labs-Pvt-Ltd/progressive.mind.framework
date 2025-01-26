package com.progressive.minds.chimera.core.databaseOps.repository.datahub;

import com.progressive.minds.chimera.core.databaseOps.model.datahub.MetadataAspectV2;
import java.util.List;
import java.util.Map;

import static com.progressive.minds.chimera.core.databaseOps.utility.RepositoryHelper.executeSelect;

public class MetadataAspectV2Repository {

    public static List<MetadataAspectV2> getConfig(Map<String, Object> filters) {
        List<MetadataAspectV2> returnList;
        returnList = executeSelect(MetadataAspectV2.class, filters);
        returnList.forEach(System.out::println);
        return returnList;
    }
}
