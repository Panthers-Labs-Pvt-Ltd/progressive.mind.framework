package com.progressive.minds.chimera.controller;

import com.progressive.minds.chimera.common.dto.GenericResponse;
import com.progressive.minds.chimera.dto.ExtractMetadata;
import com.progressive.minds.chimera.dto.ExtractMetadataResponse;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import com.progressive.minds.chimera.service.ExtractMetadataConfigService;
import java.util.List;

import javax.annotation.CheckForNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/extractMetadataConfig")
public class ExtractMetadataConfigController {

    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(ExtractMetadataConfigController.class);

    private final ExtractMetadataConfigService extractMetadataConfigService;

    @Autowired
    public ExtractMetadataConfigController(ExtractMetadataConfigService extractMetadataConfigService) {
        this.extractMetadataConfigService = extractMetadataConfigService;
    }

    // GET request - Retrieve all data source connections
    @GetMapping
    public ResponseEntity<List<ExtractMetadataResponse>> getAllExtractConfig() {
        logger.logInfo("Fetching all extract metadata.");
        return ResponseEntity.ok(extractMetadataConfigService.getExtractMetadata());
    }

    // GET request - Retrieve Extract Metadata Config By Pipeline Name
    @CheckForNull
    @GetMapping("/{pipelineName}")
    public ResponseEntity<List<ExtractMetadataResponse>> getExtractConfigByName(@PathVariable("pipelineName") String name) {
        logger.logInfo("Fetching Extract Config for pipeline: " + name);
        return ResponseEntity.ok(extractMetadataConfigService.getExtractMetadataByPipelineName(name));
    }

     // GET request - Retrieve Extract Metadata Config By Pipeline Name And Sequence Number
     @CheckForNull
     @GetMapping("/{pipelineName}/{sequenceNumber}")
     public ResponseEntity<List<ExtractMetadataResponse>> getExtractConfigByName(@PathVariable("pipelineName") String name, @PathVariable("sequenceNumber") Integer sequence) {
         logger.logInfo("Fetching Extract Config for pipeline: " + name);
         return ResponseEntity.ok(extractMetadataConfigService.getExtractMetadataByPipelineNameAndSequenceNumber(name,sequence));
     }

    // POST request - Add a new data source connection
    @PostMapping("/create")
    public ResponseEntity<GenericResponse> createConnection(@RequestBody ExtractMetadataResponse extractMetadata) {
        int createdRecords = extractMetadataConfigService.insertExtractMetadata(extractMetadata);
        if (createdRecords == 0) {
            GenericResponse response = GenericResponse.builder()
                    .message("Failed to insert the extract Metadata. Please try again.")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        GenericResponse response = GenericResponse.builder()
                .message("Extract Metadata inserted successfully for pipeline : " + extractMetadata.getPipelineName())
                .statusCode(HttpStatus.CREATED.name())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // DELETE request - Delete an existing extract metadata
    @DeleteMapping("/delete/{pipelineName}")
    public ResponseEntity<GenericResponse> deleteConnection(@PathVariable("pipelineName") String name) {
        int deletedRows = extractMetadataConfigService.deleteExtractMetadata(name);
        if (deletedRows == 0) {
            GenericResponse response = GenericResponse.builder()
                    .message("Extract Metadata for pipeline : " + name + " doesn't exist.")
                    .statusCode(HttpStatus.NOT_FOUND.name())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        GenericResponse response = GenericResponse.builder()
                .message("Extract Metadata for pipeline : " + name + " deleted successfully.")
                .statusCode(HttpStatus.OK.name())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/get/{name}")
    public ResponseEntity<List<ExtractMetadata>> getExtractConfig(@PathVariable("name") String name) {
        return ResponseEntity.ok(extractMetadataConfigService.getExtractMetadata(name));


    }


    

    // // PUT request - Update an existing data source connection
    // @PutMapping("/update")
    // public ResponseEntity<GenericResponse> updateConnection(@RequestBody DataSourceConnections updatedConnection) {
    //     String connectionId = updatedConnection.getDataSourceConnectionName();
    //     if (dataSourceConnectionsService.getConnectionByName(connectionId).isEmpty()) {
    //         GenericResponse response = GenericResponse.builder()
    //                 .message("Connection doesn't exist with the given ID: " + connectionId)
    //                 .statusCode(HttpStatus.NOT_FOUND.name())
    //                 .build();
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    //     }

    //     int updatedRows = dataSourceConnectionsService.updateConnection(updatedConnection);
    //     if (updatedRows == 0) {
    //         GenericResponse response = GenericResponse.builder()
    //                 .message("Failed to update the connection with ID: " + connectionId)
    //                 .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
    //                 .build();
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    //     }

    //     GenericResponse response = GenericResponse.builder()
    //             .message("Connection updated successfully with ID: " + connectionId)
    //             .statusCode(HttpStatus.OK.name())
    //             .build();
    //     return ResponseEntity.status(HttpStatus.OK).body(response);
    // }

    

    // // GET request - Count total connections
    // @GetMapping("/count")
    // public ResponseEntity<GenericResponse> countConnections() {
    //     long totalConnections = dataSourceConnectionsService.getTotalNumberOfConnections();
    //     GenericResponse response = GenericResponse.builder()
    //             .message("Total number of connections: " + totalConnections)
    //             .statusCode(HttpStatus.OK.name())
    //             .build();
    //     return ResponseEntity.status(HttpStatus.OK).body(response);
    // }
}
