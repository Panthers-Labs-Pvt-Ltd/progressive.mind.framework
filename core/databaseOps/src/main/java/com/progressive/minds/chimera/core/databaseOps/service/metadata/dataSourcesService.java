package com.progressive.minds.chimera.core.databaseOps.service.metadata;

import com.progressive.minds.chimera.core.databaseOps.exception.DatabaseException;
import com.progressive.minds.chimera.core.databaseOps.exception.ValidationException;
import com.progressive.minds.chimera.core.databaseOps.model.metadata.dataSources;
import com.progressive.minds.chimera.core.databaseOps.repository.metadata.dataSourcesRepository;

import java.util.List;
import java.util.Map;

public class dataSourcesService {
    private final dataSourcesRepository dataSourcesRepository;

    public dataSourcesService(dataSourcesRepository dataSourcesRepository) {
        this.dataSourcesRepository = dataSourcesRepository;
    }

    public List<dataSources> fetchAllDataSources() {
        return dataSourcesRepository.getAllDataSources();
    }

    public void createDataSource(dataSources dataSource) {
        //TODO: Validate datasource
        dataSourcesRepository.putDataSources(dataSource);
    }

    public int updateDataSource(Map<String, Object> updateFields, Map<String, Object> filters, String updatedBy) {
        if (updateFields == null || updateFields.isEmpty()) {
            throw new ValidationException("Update fields cannot be null or empty.");
        }
        if (updatedBy == null || updatedBy.isEmpty()) {
            throw new ValidationException("UpdatedBy  field cannot be null or empty.");
        }
//        if (filters == null || filters.isEmpty()) {
//            throw new ValidationException("Filters cannot be null or empty for update operation.");
//        }

        try {
            return dataSourcesRepository.updateDataSources(updateFields, filters, updatedBy);
        } catch (DatabaseException e) {
            throw new DatabaseException("Error updating users.", e);
        }
    }
    public int deleteDataSource(Map<String, Object> filters) {
        if (filters == null || filters.isEmpty()) {
            throw new ValidationException("Filters cannot be null or empty for delete operation.");
        }

        try {
            return dataSourcesRepository.deleteFromDataSources(filters);
        } catch (DatabaseException e) {
            throw new DatabaseException("Error deleting users.", e);
        }
    }

}



