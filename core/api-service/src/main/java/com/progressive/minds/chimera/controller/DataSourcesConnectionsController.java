package com.progressive.minds.chimera.controller;

import com.progressive.minds.chimera.common.dto.GenericResponse;
import com.progressive.minds.chimera.dto.DataSourceConnections;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import com.progressive.minds.chimera.service.DataSourceConnectionsService;
import java.util.List;

import javax.annotation.CheckForNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dataSourcesConnections")
public class DataSourcesConnectionsController {

    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(DataSourcesConnectionsController.class);

    private final DataSourceConnectionsService dataSourceConnectionsService;

    @Autowired
    public DataSourcesConnectionsController(DataSourceConnectionsService dataSourceConnectionsService) {
        this.dataSourceConnectionsService = dataSourceConnectionsService;
    }

    // GET request - Retrieve a data source connection by connectionId
    @CheckForNull
    @GetMapping("/{connectionName}")
    public ResponseEntity<DataSourceConnections> getConnectionById(@PathVariable("connectionName") String connectionName) {
        logger.logInfo("Fetching connection with Name: " + connectionName);
        return ResponseEntity.ok(dataSourceConnectionsService.getConnectionByName(connectionName).orElse(null));
    }

    // POST request - Add a new data source connection
    @PostMapping("/create")
    public ResponseEntity<GenericResponse> createConnection(@RequestBody DataSourceConnections connection) {
        int createdRecords = dataSourceConnectionsService.insertConnection(connection);
        if (createdRecords == 0) {
            GenericResponse response = GenericResponse.builder()
                    .message("Failed to create the connection. Please try again.")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        GenericResponse response = GenericResponse.builder()
                .message("Connection created successfully with ID: " + connection.getDataSourceConnectionName())
                .statusCode(HttpStatus.CREATED.name())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET request - Retrieve all data source connections
    @GetMapping
    public ResponseEntity<List<DataSourceConnections>> getAllConnections() {
        logger.logInfo("Fetching all data source connections.");
        return ResponseEntity.ok(dataSourceConnectionsService.getAllConnections());
    }

    // PUT request - Update an existing data source connection
    @PutMapping("/update")
    public ResponseEntity<GenericResponse> updateConnection(@RequestBody DataSourceConnections updatedConnection) {
        String connectionId = updatedConnection.getDataSourceConnectionName();
        if (dataSourceConnectionsService.getConnectionByName(connectionId).isEmpty()) {
            GenericResponse response = GenericResponse.builder()
                    .message("Connection doesn't exist with the given ID: " + connectionId)
                    .statusCode(HttpStatus.NOT_FOUND.name())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        int updatedRows = dataSourceConnectionsService.updateConnection(updatedConnection);
        if (updatedRows == 0) {
            GenericResponse response = GenericResponse.builder()
                    .message("Failed to update the connection with ID: " + connectionId)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        GenericResponse response = GenericResponse.builder()
                .message("Connection updated successfully with ID: " + connectionId)
                .statusCode(HttpStatus.OK.name())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // DELETE request - Delete a data source connection by ID
    @DeleteMapping("/delete/{connectionId}")
    public ResponseEntity<GenericResponse> deleteConnection(@PathVariable("connectionId") String connectionId) {
        int deletedRows = dataSourceConnectionsService.deleteConnection(connectionId);
        if (deletedRows == 0) {
            GenericResponse response = GenericResponse.builder()
                    .message("Connection doesn't exist with the given ID: " + connectionId)
                    .statusCode(HttpStatus.NOT_FOUND.name())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        GenericResponse response = GenericResponse.builder()
                .message("Connection with ID: " + connectionId + " deleted successfully.")
                .statusCode(HttpStatus.OK.name())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // GET request - Count total connections
    @GetMapping("/count")
    public ResponseEntity<GenericResponse> countConnections() {
        long totalConnections = dataSourceConnectionsService.getTotalNumberOfConnections();
        GenericResponse response = GenericResponse.builder()
                .message("Total number of connections: " + totalConnections)
                .statusCode(HttpStatus.OK.name())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
