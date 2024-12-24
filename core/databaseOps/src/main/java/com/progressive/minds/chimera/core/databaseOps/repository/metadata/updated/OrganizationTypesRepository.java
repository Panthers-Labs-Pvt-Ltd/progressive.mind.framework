package com.progressive.minds.chimera.core.databaseOps.repository.metadata.updated;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

import com.progressive.minds.chimera.core.databaseOps.model.metadata.updated.OrganizationTypes;
import static com.progressive.minds.chimera.core.databaseOps.utility.RepositoryHelper.*;


public class OrganizationTypesRepository {

    public List<OrganizationTypes> getAllOrganizationTypes() {
        List<OrganizationTypes> returnList = new ArrayList<>();
        int recordsProcessed = executeSelect(OrganizationTypes.class, returnList);
        return returnList;
    }

    public List<OrganizationTypes> getAllOrganizationTypes(Map<String, Object> filters) {
        List<OrganizationTypes> returnList;
            returnList = executeSelect(OrganizationTypes.class, filters);
            returnList.forEach(System.out::println);
            return returnList;
    }

    public Integer saveOrganizationTypes(OrganizationTypes organizationTypes) throws SQLException {
        int recordsImpacted = executeInsert(organizationTypes.getClass(), organizationTypes);
        return recordsImpacted;
    }

    public Integer saveOrganizationTypes(List<OrganizationTypes> organizationTypes) throws SQLException {
        int recordsImpacted = executeInsert(organizationTypes.get(0).getClass(), organizationTypes);
        return recordsImpacted;
    }

    public Integer updateOrganizationTypes(OrganizationTypes organizationTypes) throws SQLException,
            IllegalAccessException {
        int recordsImpacted = executeUpdate(organizationTypes.getClass());
        return recordsImpacted;
    }


    public Integer deleteOrganizationTypes(OrganizationTypes organizationTypes) throws SQLException,
            IllegalAccessException {
        int recordsImpacted = executeDelete(organizationTypes.getClass());
        return recordsImpacted;
    }
}
