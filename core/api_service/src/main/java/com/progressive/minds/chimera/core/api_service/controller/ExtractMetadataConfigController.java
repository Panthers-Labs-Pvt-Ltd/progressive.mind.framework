package com.progressive.minds.chimera.core.api_service.controller;

import com.progressive.minds.chimera.core.api_service.common.dto.GenericResponse;
import com.progressive.minds.chimera.core.api_service.dto.ExtractMetadata;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import com.progressive.minds.chimera.core.api_service.service.ExtractMetadataConfigService;
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
    public ResponseEntity<List<ExtractMetadata>> getAllExtractConfig() {
        logger.logInfo("Fetching all extract metadata.");
        return ResponseEntity.ok(extractMetadataConfigService.getExtractMetadata());
    }

    // GET request - Retrieve Extract Metadata Config By Pipeline Name
    @CheckForNull
    @GetMapping("/{pipelineName}")
    public ResponseEntity<List<ExtractMetadata>> getExtractConfigByName(@PathVariable("pipelineName") String name) {
        logger.logInfo("Fetching Extract Config for pipeline: " + name);
        return ResponseEntity.ok(extractMetadataConfigService.getExtractMetadata(name));
    }

     // GET request - Retrieve Extract Metadata Config By Pipeline Name And Sequence Number
     @CheckForNull
     @GetMapping("/{pipelineName}/{sequenceNumber}")
     public ResponseEntity<ExtractMetadata> getExtractConfigByName(@PathVariable("pipelineName") String name, @PathVariable("sequenceNumber") Integer sequence) {
         logger.logInfo("Fetching Extract Config for pipeline: " + name);
         return ResponseEntity.ok(extractMetadataConfigService.getExtractMetadataByPipelineNameAndSequenceNumber(name,sequence));
     }

    // POST request - Add a new data source connection
    @PostMapping("/create")
    public ResponseEntity<GenericResponse> createConnection(@RequestBody ExtractMetadata extractMetadata) {
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

    // PUT request - Update an existing pipeline by name
    @PutMapping("/update")
    public ResponseEntity<GenericResponse> updateExtractConfig(@RequestBody ExtractMetadata updatedConfig) {
        String pipelineName = updatedConfig.getPipelineName();
        int sequenceNumber = updatedConfig.getSequenceNumber();

        if (extractMetadataConfigService.getExtractMetadataByPipelineNameAndSequenceNumber(pipelineName, sequenceNumber) == null) {
        // Pipeline does not exist
        GenericResponse genericResponse = GenericResponse.builder()
            .message("Extract Config doesn't exist for pipeline: " + pipelineName + " and sequence: " + sequenceNumber)
            .statusCode(HttpStatus.NOT_FOUND.name())
            .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }

        int updatedRows = extractMetadataConfigService.updateExtractMetadata(updatedConfig);
        if (updatedRows == 0) {
        // Update operation didn't affect any rows
        GenericResponse genericResponse = GenericResponse.builder()
            .message("Extract Config update failed for pipeline: " + pipelineName + " and sequence: " + sequenceNumber)
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
            .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genericResponse);
        }

        // Update operation succeeded
        GenericResponse genericResponse = GenericResponse.builder()
            .message("Extract Config updated successfully for pipeline: " + pipelineName + " and sequence: " + sequenceNumber)
            .statusCode(HttpStatus.OK.name())
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
    }

    @DeleteMapping("/delete/{pipeLineName}/{sequence}")
    public ResponseEntity<GenericResponse> deletePipeline(@PathVariable("pipeLineName") String pipelineName, @PathVariable("sequence") int sequence) {
        if (extractMetadataConfigService.deleteExtractMetadata(pipelineName, sequence) == 0) {
        GenericResponse genericResponse = GenericResponse.builder()
            .message("Extract Config doesn't exist with the given pipeline name: " + pipelineName + " and sequence: " + sequence)
            .statusCode(HttpStatus.NOT_FOUND.name())
            .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        } else {
        GenericResponse genericResponse = GenericResponse.builder()
            .message("Extract Config deleted successfully: " + pipelineName + " and sequence: " + sequence)
            .statusCode(HttpStatus.OK.name())
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
        }
    }

    

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
