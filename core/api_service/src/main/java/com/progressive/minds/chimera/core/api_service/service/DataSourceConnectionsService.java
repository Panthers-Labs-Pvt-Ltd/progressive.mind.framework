package com.progressive.minds.chimera.core.api_service.service;

import static com.progressive.minds.chimera.core.api_service.entity.DataSourcesConnectionsDynamicSqlEntity.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import com.progressive.minds.chimera.core.api_service.dto.DataSourceConnections;
import com.progressive.minds.chimera.core.api_service.repository.DataSourceConnectionsDBMapper;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DataSourceConnectionsService {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceConnectionsService.class);

    private final DataSourceConnectionsDBMapper dataSourceConnectionsDBMapper;

    public DataSourceConnectionsService(DataSourceConnectionsDBMapper dataSourcesConnectionsDBMapper) {
        this.dataSourceConnectionsDBMapper = dataSourcesConnectionsDBMapper;
    }

    public long getTotalNumberOfConnections() {
        logger.info("Fetching total number of data source connections.");
        SelectStatementProvider countStatementProvider = select(count())
            .from(dataSourceConnection)
            .build()
            .render(RenderingStrategies.MYBATIS3);

        long count = dataSourceConnectionsDBMapper.count(countStatementProvider);
        logger.info("Total data source connections count: {}", count);
        return count;
    }

    public Optional<DataSourceConnections> getConnectionByName(String connectionName) {
        logger.info("Fetching connection with name: {}", connectionName);
        SelectStatementProvider selectStatement = select(dataSourceConnection.allColumns())
            .from(dataSourceConnection)
            .where(dataSourceConnectionName, isEqualTo(connectionName))
            .build()
            .render(RenderingStrategies.MYBATIS3);

        return dataSourceConnectionsDBMapper.selectOne(selectStatement);
    }

    public List<DataSourceConnections> getAllConnections() {
        logger.info("Fetching all data source connections.");
        SelectStatementProvider selectStatement = select(dataSourceConnection.allColumns())
            .from(dataSourceConnection)
            .build()
            .render(RenderingStrategies.MYBATIS3);

        return dataSourceConnectionsDBMapper.selectMany(selectStatement);
    }

    public int insertConnection(DataSourceConnections connection) {
        logger.info("Inserting new data source connection: {}", connection);
        InsertStatementProvider<DataSourceConnections> insertStatement = insert(connection)
            .into(dataSourceConnection)
            .map(dataSourceConnectionName).toProperty("dataSourceConnectionName")
            .map(dataSourceType).toProperty("dataSourceType")
            .map(dataSourceSubType).toProperty("dataSourceSubType")
            .map(authenticationType).toProperty("authenticationType")
            .map(authenticationData).toProperty("authenticationData")
            .map(connectionMetadata).toProperty("connectionMetadata")
            .map(userReadConfig).toProperty("userReadConfig")
            .map(userWriteConfig).toProperty("userWriteConfig")
            .map(description).toProperty("description")
            .map(activeFlag).toProperty("activeFlag")
            .map(createdTimestamp).toConstant("'" + new Timestamp(System.currentTimeMillis()).toString() + "'")
            .map(createdBy).toProperty("createdBy")
            .build()
            .render(RenderingStrategies.MYBATIS3);

        return dataSourceConnectionsDBMapper.insert(insertStatement);
    }

    public int updateConnection(DataSourceConnections connection) {
        logger.info("Updating data source connection with ID: {}", connection.getDataSourceConnectionName());
        UpdateStatementProvider updateStatement = update(dataSourceConnection)
            .set(dataSourceType).equalToWhenPresent(connection.getDataSourceType())
            .set(dataSourceSubType).equalToWhenPresent(connection.getDataSourceSubType())
            .set(authenticationType).equalToWhenPresent(connection.getAuthenticationType())
            .set(authenticationData).equalToWhenPresent(connection.getAuthenticationData())
            .set(connectionMetadata).equalToWhenPresent(connection.getConnectionMetadata())
            .set(userReadConfig).equalToWhenPresent(connection.getUserReadConfig())
            .set(userWriteConfig).equalToWhenPresent(connection.getUserWriteConfig())
            .set(description).equalToWhenPresent(connection.getDescription())
            .set(activeFlag).equalToWhenPresent(connection.getActiveFlag())
            .set(updatedBy).equalToWhenPresent(connection.getUpdatedBy())
            .set(updatedTimestamp).equalTo(new Timestamp(System.currentTimeMillis()))
            .where(dataSourceConnectionName, isEqualTo(connection.getDataSourceConnectionName()))
            .build()
            .render(RenderingStrategies.MYBATIS3);
        return dataSourceConnectionsDBMapper.update(updateStatement);
    }

    public int deleteConnection(String connectionName) {
        logger.info("Deleting data source connection with ID: {}", connectionName);
        DeleteStatementProvider deleteStatement = deleteFrom(dataSourceConnection)
            .where(dataSourceConnectionName, isEqualTo(connectionName))
            .build()
            .render(RenderingStrategies.MYBATIS3);

        return dataSourceConnectionsDBMapper.delete(deleteStatement);
    }
}
