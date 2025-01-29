package com.progressive.minds.chimera.controller;

import com.progressive.minds.chimera.common.dto.GenericResponse;
import com.progressive.minds.chimera.dto.PersistMetadataConfig;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import com.progressive.minds.chimera.service.persistMetadataConfigService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/persistMetadataConfig")
public class PersistMetadataConfigController {

    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(PipelineController.class);

    private persistMetadataConfigService persistMetadataConfigService;

    @Autowired
    public PersistMetadataConfigController(persistMetadataConfigService persistMetadataConfigService) {
        this.persistMetadataConfigService = persistMetadataConfigService;
    }

    // GET request - Retrieve an existing config by pipeline name
    @GetMapping("/{name}")
    public ResponseEntity<List<PersistMetadataConfig>> getPersistMetadataConfigByName(@PathVariable("name") String name) {
        logger.logInfo("Fetching persist Metadata for pipeline: " + name + " from the database.");
        return ResponseEntity.ok(persistMetadataConfigService.getPersistMetadataByPipelineName(name));
    }

    // GET request - Retrieve an existing config by pipeline name and id
    @GetMapping("/{name}/{sequence}")
    public ResponseEntity<PersistMetadataConfig> getPersistMetadataConfigByName(@PathVariable("name") String name, @PathVariable("sequence") int sequence) {
        logger.logInfo("Fetching persist Metadata for pipeline: " + name + " and sequence: " + sequence + " from the database.");
        return ResponseEntity.ok(persistMetadataConfigService.getPersistMetadataByPipelineName(name, sequence));
    }

    // POST request - Add a new pipeline
    @PostMapping("/create")
    public ResponseEntity<GenericResponse> createPipeline(@RequestBody PersistMetadataConfig persistMetadataConfig) {
        int numberOfRecordsCreated = persistMetadataConfigService.insertConfig(persistMetadataConfig);
        if (numberOfRecordsCreated == 0) {
            GenericResponse genericResponse = GenericResponse.builder()
            .message("Failed to create persist config. Please try again.")
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
            .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genericResponse);
        }

        GenericResponse genericResponse = GenericResponse.builder()
            .message("Persist Config created successfully for pipeline: " + persistMetadataConfig.getPipelineName() + " with sequence: " + persistMetadataConfig.getSequenceNumber())
            .statusCode(HttpStatus.CREATED.name())
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(genericResponse);
    }

    // GET request - Retrieve all pipelines
    @GetMapping
    public ResponseEntity<List<PersistMetadataConfig>> getAllPersistMetadataConfig() {
        return ResponseEntity.ok(persistMetadataConfigService.getAllPersistMetadataConfig());
    }

    // PUT request - Update an existing pipeline by name
    @PutMapping("/update")
    public ResponseEntity<GenericResponse> updatePipeline(@RequestBody PersistMetadataConfig updatedConfig) {
        String pipelineName = updatedConfig.getPipelineName();
        int sequenceNumber = updatedConfig.getSequenceNumber();

        if (persistMetadataConfigService.getPersistMetadataByPipelineName(pipelineName, sequenceNumber) == null) {
        // Pipeline does not exist
        GenericResponse genericResponse = GenericResponse.builder()
            .message("Persist Config doesn't exist for pipeline: " + pipelineName + " and sequence: " + sequenceNumber)
            .statusCode(HttpStatus.NOT_FOUND.name())
            .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }

        int updatedRows = persistMetadataConfigService.updateConfig(updatedConfig);
        if (updatedRows == 0) {
        // Update operation didn't affect any rows
        GenericResponse genericResponse = GenericResponse.builder()
            .message("Persist Config update failed for pipeline: " + pipelineName + " and sequence: " + sequenceNumber)
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
            .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genericResponse);
        }

        // Update operation succeeded
        GenericResponse genericResponse = GenericResponse.builder()
            .message("Persist Config updated successfully for pipeline: " + pipelineName + " and sequence: " + sequenceNumber)
            .statusCode(HttpStatus.OK.name())
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
    }

    // Delete request -delete pipeline
    @DeleteMapping("/delete/{pipeLineName}")
    public ResponseEntity<GenericResponse> deletePipeline(@PathVariable("pipeLineName") String pipelineName) {
        if (persistMetadataConfigService.deleteConfig(pipelineName) == 0) {
        GenericResponse genericResponse = GenericResponse.builder()
            .message("Persist Config doesn't exist with the given pipeline name: " + pipelineName)
            .statusCode(HttpStatus.NOT_FOUND.name())
            .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        } else {
        GenericResponse genericResponse = GenericResponse.builder()
            .message("Persist Config deleted successfully: " + pipelineName)
            .statusCode(HttpStatus.OK.name())
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
        }
    }

    // Delete request -delete pipeline
    @DeleteMapping("/delete/{pipeLineName}/{sequence}")
    public ResponseEntity<GenericResponse> deletePipeline(@PathVariable("pipeLineName") String pipelineName, @PathVariable("sequence") int sequence) {
        if (persistMetadataConfigService.deleteConfig(pipelineName, sequence) == 0) {
        GenericResponse genericResponse = GenericResponse.builder()
            .message("Persist Config doesn't exist with the given pipeline name: " + pipelineName + " and sequence: " + sequence)
            .statusCode(HttpStatus.NOT_FOUND.name())
            .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        } else {
        GenericResponse genericResponse = GenericResponse.builder()
            .message("Persist Config deleted successfully: " + pipelineName + " and sequence: " + sequence)
            .statusCode(HttpStatus.OK.name())
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
        }
    }

    // Delete request -delete pipeline
    @GetMapping("/count")
    public ResponseEntity<GenericResponse> countNumberOfDataPipeline() {
        long totalNumberOfPipeline = persistMetadataConfigService.getTotalNumberOfDataSources();
        GenericResponse genericResponse = GenericResponse.builder()
            .message("Number of PersistConfigs " + totalNumberOfPipeline)
            .statusCode(HttpStatus.OK.name())
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
    }

}

