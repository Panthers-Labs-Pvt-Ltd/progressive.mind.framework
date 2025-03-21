package com.progressive.minds.chimera.core.api_service.controller;

import com.progressive.minds.chimera.core.api_service.common.dto.GenericResponse;
import com.progressive.minds.chimera.core.api_service.dto.DataPipeline;
import com.progressive.minds.chimera.core.api_service.dto.PipelineMetadata;
import com.progressive.minds.chimera.core.api_service.service.PipelineService;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Pipeline API", description = "Endpoints for managing data pipelines")
@RestController
@RequestMapping("/api/v1/pipelines")
public class PipelineController {

  private static final ChimeraLogger logger =
      ChimeraLoggerFactory.getLogger(PipelineController.class);

  private final PipelineService pipelineService;

  @Autowired
  public PipelineController(PipelineService pipelineService) {
    this.pipelineService = pipelineService;
  }

  @GetMapping("/getDetails/{name}")
  public ResponseEntity<PipelineMetadata> getPipelineMetadata(@PathVariable("name") String name) {
    logger.logInfo("Fetching Pipeline Metadata for pipeline: " + name + " from the database.");
    return ResponseEntity.ok(pipelineService.getPipelineMetadata(name));
  }

  @GetMapping("/{name}")
  public ResponseEntity<DataPipeline> getPipelineByName(@PathVariable("name") String name) {
    logger.logInfo("Fetching pipeline with name: " + name + " from the database.");
    return ResponseEntity.ok(pipelineService.getDataPipeLineByName(name));
  }

  @PostMapping(
      path = "/create",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<GenericResponse> createPipeline(@RequestBody DataPipeline pipeline) {
    int numberOfRecordsCreated = pipelineService.insertPipeline(pipeline);
    if (numberOfRecordsCreated == 0) {
      GenericResponse genericResponse =
          GenericResponse.builder()
              .message("Failed to create pipeline. Please try again.")
              .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
              .build();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genericResponse);
    }

    GenericResponse genericResponse =
        GenericResponse.builder()
            .message("Pipeline created successfully.")
            .statusCode(HttpStatus.OK.name())
            .build();
    return ResponseEntity.ok(genericResponse);
  }

  @GetMapping
  public ResponseEntity<List<DataPipeline>> getAllPipelines() {
    return ResponseEntity.ok(pipelineService.getAllPipelines());
  }

  @PutMapping(
      path = "/update",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<GenericResponse> updatePipeline(@RequestBody DataPipeline updatedPipeline) {
    String pipelineName = updatedPipeline.getPipelineName();

    if (pipelineService.getDataPipeLineByName(pipelineName) == null) {
      // Pipeline does not exist
      GenericResponse genericResponse =
          GenericResponse.builder()
              .message("Pipeline doesn't exist with the given name: " + pipelineName)
              .statusCode(HttpStatus.NOT_FOUND.name())
              .build();
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
    }

    int updatedRows = pipelineService.updatePipeline(updatedPipeline);
    if (updatedRows == 0) {
      GenericResponse genericResponse =
          GenericResponse.builder()
              .message("Pipeline update failed for name: " + pipelineName)
              .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
              .build();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genericResponse);
    }

    GenericResponse genericResponse =
        GenericResponse.builder()
            .message("Pipeline updated successfully with name: " + pipelineName)
            .statusCode(HttpStatus.OK.name())
            .build();
    return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
  }

  @DeleteMapping("/delete/{pipeLineName}")
  public ResponseEntity<GenericResponse> deletePipeline(
      @PathVariable("pipeLineName") String pipelineName) {
    if (pipelineService.deletePipeline(pipelineName) == 0) {
      GenericResponse genericResponse =
          GenericResponse.builder()
              .message("Pipeline doesn't exist with the given name: " + pipelineName)
              .statusCode(HttpStatus.NOT_FOUND.name())
              .build();
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
    } else {
      GenericResponse genericResponse =
          GenericResponse.builder()
              .message("Pipeline deleted successfully: " + pipelineName)
              .statusCode(HttpStatus.OK.name())
              .build();
      return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
    }
  }

  @GetMapping("/count")
  public ResponseEntity<GenericResponse> countNumberOfDataPipeline() {
    long totalNumberOfPipeline = pipelineService.getTotalNumberOfPipeline();
    GenericResponse genericResponse =
        GenericResponse.builder()
            .message("Number of Data pipelines " + totalNumberOfPipeline)
            .statusCode(HttpStatus.OK.name())
            .build();
    return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
  }
}
