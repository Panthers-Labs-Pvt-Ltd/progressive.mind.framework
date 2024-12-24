package com.progressive.minds.chimera.core.databaseOps.repository.metadata.updated;

import java.util.List;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

import com.progressive.minds.chimera.core.databaseOps.exception.DatabaseException;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.updated.OrganizationTypes;

import static com.progressive.minds.chimera.core.databaseOps.utility.RepositoryHelper.*;


public class OrganizationTypesRepository {

    public List<OrganizationTypes> getAllOrganizationTypes() {
        List<OrganizationTypes> returnList = new ArrayList<>();
        int recordsProcessed = executeSelect(OrganizationTypes.class, returnList);

        System.out.println("Records processed: " + recordsProcessed);
        returnList.forEach(System.out::println);
        return returnList;
    }

    public List<OrganizationTypes> getAllOrganizationTypes(Map<String, Object> filters) {
        List<OrganizationTypes> returnList;
        try {
            returnList = executeSelect(OrganizationTypes.class, filters);
            returnList.forEach(System.out::println);

        } catch (Exception ex) {
            String errorMessage = "Unexpected error while saving data sources Connections: " + ex.getMessage();
            throw new DatabaseException(errorMessage, ex);
        }
        return returnList;
    }

    public void saveOrganizationTypes(OrganizationTypes organizationTypes) throws SQLException {
        int recordsImpacted = executeInsert(organizationTypes.getClass(), organizationTypes);
        System.out.println("Total Records " + recordsImpacted);
    }

    public void saveOrganizationTypes(List<OrganizationTypes> organizationTypes) throws SQLException {
        int recordsImpacted = executeInsert(organizationTypes.get(0).getClass(), organizationTypes);
        System.out.println("Total Records " + recordsImpacted);
    }

    public void updateOrganizationTypes(OrganizationTypes organizationTypes) throws SQLException,
            IllegalAccessException {
        int recordsImpacted = executeUpdate(organizationTypes.getClass());
        System.out.println("Total Records " + recordsImpacted);
    }


    public void deleteOrganizationTypes(OrganizationTypes organizationTypes) throws SQLException,
            IllegalAccessException {
        int recordsImpacted = executeDelete(organizationTypes.getClass());
        System.out.println("Total Records " + recordsImpacted);
    }
}
