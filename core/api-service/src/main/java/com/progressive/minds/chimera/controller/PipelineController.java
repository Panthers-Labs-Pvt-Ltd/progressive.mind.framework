package com.progressive.minds.chimera.controller;

import com.progressive.minds.chimera.common.dto.GenericResponse;
import com.progressive.minds.chimera.dto.DataPipeline;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import com.progressive.minds.chimera.service.PipelineService;
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
@RequestMapping("/api/v1/pipelines")
public class PipelineController {

  private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(PipelineController.class);

  private PipelineService pipelineService;

  @Autowired
  public PipelineController(PipelineService pipelineService) {
    this.pipelineService = pipelineService;
  }

  // GET request - Retrieve an existing pipeline by ID
  @GetMapping("/{name}")
  public ResponseEntity<DataPipeline> getPipelineByName(@PathVariable("name") String name) {
    logger.logInfo("Fetching pipeline with name: " + name + " from the database.");
    return ResponseEntity.ok(pipelineService.getDataPipeLineByName(name));
  }

  // POST request - Add a new pipeline
  @PostMapping("/create")
  public ResponseEntity<GenericResponse> createPipeline(@RequestBody DataPipeline pipeline) {
    int numberOfRecordsCreated = pipelineService.insertPipeline(pipeline);
    if (numberOfRecordsCreated == 0) {
      GenericResponse genericResponse = GenericResponse.builder()
          .message("Failed to create pipeline. Please try again.")
          .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
          .build();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genericResponse);
    }

    GenericResponse genericResponse = GenericResponse.builder()
        .message("Pipeline created successfully with pipeline name: " + pipeline.getPipelineName())
        .statusCode(HttpStatus.CREATED.name())
        .build();
    return ResponseEntity.status(HttpStatus.CREATED).body(genericResponse);
  }

  // GET request - Retrieve all pipelines
  @GetMapping
  public ResponseEntity<List<DataPipeline>> getAllPipelines() {
    return ResponseEntity.ok(pipelineService.getAllPipelines());
  }

  // PUT request - Update an existing pipeline by name
  @PutMapping("/update")
  public ResponseEntity<GenericResponse> updatePipeline(@RequestBody DataPipeline updatedPipeline) {
    String pipelineName = updatedPipeline.getPipelineName();

    if (pipelineService.getDataPipeLineByName(pipelineName) == null) {
      // Pipeline does not exist
      GenericResponse genericResponse = GenericResponse.builder()
          .message("Pipeline doesn't exist with the given name: " + pipelineName)
          .statusCode(HttpStatus.NOT_FOUND.name())
          .build();
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
    }

    int updatedRows = pipelineService.updatePipeline(updatedPipeline);
    if (updatedRows == 0) {
      // Update operation didn't affect any rows
      GenericResponse genericResponse = GenericResponse.builder()
          .message("Pipeline update failed for name: " + pipelineName)
          .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
          .build();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genericResponse);
    }

    // Update operation succeeded
    GenericResponse genericResponse = GenericResponse.builder()
        .message("Pipeline updated successfully with name: " + pipelineName)
        .statusCode(HttpStatus.OK.name())
        .build();
    return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
  }

  // Delete request -delete pipeline
  @DeleteMapping("/delete/{pipeLineName}")
  public ResponseEntity<GenericResponse> deletePipeline(@PathVariable("pipeLineName") String pipelineName) {
    if (pipelineService.deletePipeline(pipelineName) == 0) {
      GenericResponse genericResponse = GenericResponse.builder()
          .message("Pipeline doesn't exist with the given name: " + pipelineName)
          .statusCode(HttpStatus.NOT_FOUND.name())
          .build();
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
    } else {
      GenericResponse genericResponse = GenericResponse.builder()
          .message("Pipeline deleted successfully: " + pipelineName)
          .statusCode(HttpStatus.OK.name())
          .build();
      return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
    }
  }

  // Delete request -delete pipeline
  @GetMapping("/count")
  public ResponseEntity<GenericResponse> countNumberOfDataPipeline() {
    long totalNumberOfPipeline = pipelineService.getTotalNumberOfPipeline();
    GenericResponse genericResponse = GenericResponse.builder()
        .message("Number of Data pipelines " + totalNumberOfPipeline)
        .statusCode(HttpStatus.OK.name())
        .build();
    return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
  }

}

