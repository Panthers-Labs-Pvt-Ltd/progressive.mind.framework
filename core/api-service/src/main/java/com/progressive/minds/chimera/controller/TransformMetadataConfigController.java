package com.progressive.minds.chimera.controller;

import com.progressive.minds.chimera.common.dto.GenericResponse;
import com.progressive.minds.chimera.dto.TransformMetadataConfig;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import com.progressive.minds.chimera.service.TransformMetadataConfigService;
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
@RequestMapping("/api/v1/transformMetadataConfig")
public class TransformMetadataConfigController {
    
    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(PipelineController.class);

    private TransformMetadataConfigService transformMetadataConfigService;

    @Autowired
    public TransformMetadataConfigController(TransformMetadataConfigService transformMetadataConfigService) {
        this.transformMetadataConfigService = transformMetadataConfigService;
    }

    // GET request - Retrieve an existing config by pipeline name
    @GetMapping("/{name}")
    public ResponseEntity<List<TransformMetadataConfig>> getTransformMetadataConfigByName(@PathVariable("name") String name) {
        logger.logInfo("Fetching transform Metadata for pipeline: " + name + " from the database.");
        return ResponseEntity.ok(transformMetadataConfigService.getTransformMetadataByPipelineName(name));
    }

    // GET request - Retrieve an existing config by pipeline name and id
    @GetMapping("/{name}/{sequence}")
    public ResponseEntity<TransformMetadataConfig> getTransformMetadataConfigByName(@PathVariable("name") String name, @PathVariable("sequence") int sequence) {
        logger.logInfo("Fetching transform Metadata for pipeline: " + name + " and sequence: " + sequence + " from the database.");
        return ResponseEntity.ok(transformMetadataConfigService.getTransformMetadataByPipelineName(name, sequence));
    }

    // POST request - Add a new pipeline
    @PostMapping("/create")
    public ResponseEntity<GenericResponse> createPipeline(@RequestBody TransformMetadataConfig transformMetadataConfig) {
        int numberOfRecordsCreated = transformMetadataConfigService.insertConfig(transformMetadataConfig);
        if (numberOfRecordsCreated == 0) {
            GenericResponse genericResponse = GenericResponse.builder()
            .message("Failed to create transform config. Please try again.")
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
            .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genericResponse);
        }

        GenericResponse genericResponse = GenericResponse.builder()
            .message("Transform Config created successfully for pipeline: " + transformMetadataConfig.getPipelineName() + " with sequence: " + transformMetadataConfig.getSequenceNumber())
            .statusCode(HttpStatus.CREATED.name())
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(genericResponse);
    }

    // GET request - Retrieve all pipelines
    @GetMapping
    public ResponseEntity<List<TransformMetadataConfig>> getAllPersistMetadataConfig() {
        return ResponseEntity.ok(transformMetadataConfigService.getAllTransformMetadataConfig());
    }

    // PUT request - Update an existing pipeline by name
    @PutMapping("/update")
    public ResponseEntity<GenericResponse> updatePipeline(@RequestBody TransformMetadataConfig updatedConfig) {
        String pipelineName = updatedConfig.getPipelineName();
        int sequenceNumber = updatedConfig.getSequenceNumber();

        if (transformMetadataConfigService.getTransformMetadataByPipelineName(pipelineName, sequenceNumber) == null) {
        // Pipeline does not exist
        GenericResponse genericResponse = GenericResponse.builder()
            .message("Transform Config doesn't exist for pipeline: " + pipelineName + " and sequence: " + sequenceNumber)
            .statusCode(HttpStatus.NOT_FOUND.name())
            .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }

        int updatedRows = transformMetadataConfigService.updateConfig(updatedConfig);
        if (updatedRows == 0) {
        // Update operation didn't affect any rows
        GenericResponse genericResponse = GenericResponse.builder()
            .message("Transform Config update failed for pipeline: " + pipelineName + " and sequence: " + sequenceNumber)
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
            .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genericResponse);
        }

        // Update operation succeeded
        GenericResponse genericResponse = GenericResponse.builder()
            .message("Transform Config updated successfully for pipeline: " + pipelineName + " and sequence: " + sequenceNumber)
            .statusCode(HttpStatus.OK.name())
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
    }

    // Delete request -delete pipeline
    @DeleteMapping("/delete/{pipeLineName}")
    public ResponseEntity<GenericResponse> deletePipeline(@PathVariable("pipeLineName") String pipelineName) {
        if (transformMetadataConfigService.deleteConfig(pipelineName) == 0) {
        GenericResponse genericResponse = GenericResponse.builder()
            .message("Transform Config doesn't exist with the given pipeline name: " + pipelineName)
            .statusCode(HttpStatus.NOT_FOUND.name())
            .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        } else {
        GenericResponse genericResponse = GenericResponse.builder()
            .message("Transform Config deleted successfully: " + pipelineName)
            .statusCode(HttpStatus.OK.name())
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
        }
    }

    // Delete request -delete pipeline
    @DeleteMapping("/delete/{pipeLineName}/{sequence}")
    public ResponseEntity<GenericResponse> deletePipeline(@PathVariable("pipeLineName") String pipelineName, @PathVariable("sequence") int sequence) {
        if (transformMetadataConfigService.deleteConfig(pipelineName, sequence) == 0) {
        GenericResponse genericResponse = GenericResponse.builder()
            .message("Transform Config doesn't exist with the given pipeline name: " + pipelineName + " and sequence: " + sequence)
            .statusCode(HttpStatus.NOT_FOUND.name())
            .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        } else {
        GenericResponse genericResponse = GenericResponse.builder()
            .message("Transform Config deleted successfully: " + pipelineName + " and sequence: " + sequence)
            .statusCode(HttpStatus.OK.name())
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
        }
    }

    // Delete request -delete pipeline
    @GetMapping("/count")
    public ResponseEntity<GenericResponse> countNumberOfDataPipeline() {
        long totalNumberOfPipeline = transformMetadataConfigService.getTotalNumberOfTransformMetadataConfig();
        GenericResponse genericResponse = GenericResponse.builder()
            .message("Number of TransformConfigs " + totalNumberOfPipeline)
            .statusCode(HttpStatus.OK.name())
            .build();
        return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
    }

}

