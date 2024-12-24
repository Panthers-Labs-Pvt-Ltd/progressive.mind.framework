package com.progressive.minds.chimera.core.databaseOps.repository.metadata.updated;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.updated.OrganizationHierarchy;

import static com.progressive.minds.chimera.core.databaseOps.utility.RepositoryHelper.*;


public class OrganizationHierarchyRepository {

    public List<OrganizationHierarchy> getAllOrganizationHierarchy() {
        List<OrganizationHierarchy> returnList = new ArrayList<>();
        int recordsProcessed = executeSelect(OrganizationHierarchy.class, returnList);
        return returnList;
    }

    public List<OrganizationHierarchy> getAllOrganizationHierarchy(Map<String, Object> filters) {
        List<OrganizationHierarchy> returnList;
            returnList = executeSelect(OrganizationHierarchy.class, filters);
            return returnList;
    }

    public Integer saveOrganizationHierarchy(OrganizationHierarchy organizationHierarchy) throws SQLException {
        int recordsImpacted = executeInsert(organizationHierarchy.getClass(), organizationHierarchy);
        return recordsImpacted;
    }

    public Integer saveOrganizationHierarchy(List<OrganizationHierarchy> organizationHierarchy) throws SQLException {
        int recordsImpacted = executeInsert(organizationHierarchy.get(0).getClass(), organizationHierarchy);
        return recordsImpacted;
    }

    public Integer updateOrganizationHierarchy(OrganizationHierarchy organizationHierarchy) throws SQLException,
            IllegalAccessException {
        int recordsImpacted = executeUpdate(organizationHierarchy.getClass());
        return recordsImpacted;
    }


    public Integer deleteOrganizationHierarchy(OrganizationHierarchy organizationHierarchy) throws SQLException,
            IllegalAccessException {
        int recordsImpacted = executeDelete(organizationHierarchy.getClass());
        return recordsImpacted;
    }
}
