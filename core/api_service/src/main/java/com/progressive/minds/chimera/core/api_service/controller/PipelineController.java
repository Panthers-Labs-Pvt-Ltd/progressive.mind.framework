package com.progressive.minds.chimera.core.api_service.controller;

import com.progressive.minds.chimera.core.api_service.common.dto.GenericResponse;
import com.progressive.minds.chimera.core.api_service.dto.DataPipeline;
import com.progressive.minds.chimera.core.api_service.dto.PipelineMetadata;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import com.progressive.minds.chimera.core.api_service.service.PipelineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Pipeline API", description = "Endpoints for managing data pipelines")
@RestController
@RequestMapping("/api/v1/pipelines")
public class PipelineController {

  private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(PipelineController.class);

  private final PipelineService pipelineService;
 
  @Autowired
  public PipelineController(PipelineService pipelineService) {
    this.pipelineService = pipelineService;
  }

  @Operation(summary = "Get a pipeline by name", description = "Retrieve an existing pipeline by its name")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Pipeline retrieved successfully",
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataPipeline.class))),
    @ApiResponse(responseCode = "404", description = "Pipeline not found")
  })

  @GetMapping("/getDetails/{name}")
  public ResponseEntity<PipelineMetadata> getPipelineMetadata(
                                               @Parameter(description = "Name of the pipeline to retrieve", required = true)
                                               @PathVariable("name") String name) {
    logger.logInfo("Fetching Pipeline Metadata for pipeline: " + name + " from the database.");
    return ResponseEntity.ok(pipelineService.getPipelineMetadata(name));
  }

  @GetMapping("/{name}")
  public ResponseEntity<DataPipeline> getPipelineByName(
      @Parameter(description = "Name of the pipeline to retrieve", required = true) @PathVariable("name") String name) {
    logger.logInfo("Fetching pipeline with name: " + name + " from the database.");
    return ResponseEntity.ok(pipelineService.getDataPipeLineByName(name));
  }

  @Operation(summary = "Create a new pipeline", description = "Add a new pipeline")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Pipeline created successfully",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class))),
      @ApiResponse(responseCode = "500", description = "Failed to create pipeline")
  })
  @PostMapping("/create")
  public ResponseEntity<GenericResponse> createPipeline(
      @Parameter(description = "Pipeline object to be created", required = true) @RequestBody DataPipeline pipeline) {
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

  @Operation(summary = "Get all pipelines", description = "Retrieve a list of all pipelines")
  @ApiResponse(responseCode = "200", description = "Pipelines retrieved successfully",
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataPipeline.class)))
  @GetMapping
  public ResponseEntity<List<DataPipeline>> getAllPipelines() {
    return ResponseEntity.ok(pipelineService.getAllPipelines());
  }

  @Operation(summary = "Update an existing pipeline", description = "Update the details of an existing pipeline by name")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Pipeline updated successfully",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class))),
      @ApiResponse(responseCode = "404", description = "Pipeline not found"),
      @ApiResponse(responseCode = "500", description = "Pipeline update failed")
  })
  @PutMapping("/update")
  public ResponseEntity<GenericResponse> updatePipeline(
      @Parameter(description = "Updated pipeline object", required = true) @RequestBody DataPipeline updatedPipeline) {
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
      GenericResponse genericResponse = GenericResponse.builder()
          .message("Pipeline update failed for name: " + pipelineName)
          .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
          .build();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genericResponse);
    }

    GenericResponse genericResponse = GenericResponse.builder()
        .message("Pipeline updated successfully with name: " + pipelineName)
        .statusCode(HttpStatus.OK.name())
        .build();
    return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
  }

  @Operation(summary = "Delete a pipeline", description = "Delete an existing pipeline by name")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Pipeline deleted successfully",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class))),
      @ApiResponse(responseCode = "404", description = "Pipeline not found")
  })
  @DeleteMapping("/delete/{pipeLineName}")
  public ResponseEntity<GenericResponse> deletePipeline(
      @Parameter(description = "Name of the pipeline to delete", required = true) @PathVariable("pipeLineName") String pipelineName) {
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

  @Operation(summary = "Count pipelines", description = "Retrieve the total number of pipelines")
  @ApiResponse(responseCode = "200", description = "Count retrieved successfully",
      content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class)))
  @GetMapping("/count")
  public ResponseEntity<GenericResponse> countNumberOfDataPipeline() {
    long totalNumberOfPipeline = pipelineService.getTotalNumberOfPipeline();
    GenericResponse genericResponse = GenericResponse.builder()
        .message("Number of Data pipelines " + totalNumberOfPipeline)
        .statusCode(HttpStatus.OK.name())
        .build();
    return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
  }

  // @Operation(summary = "Create a parent-child record", description = "Test the parent-child creation")
  // @ApiResponse(responseCode = "200", description = "Parent-child record created successfully",
  //     content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenericResponse.class)))
  // @PostMapping("/create/parent-child")
  // public ResponseEntity<GenericResponse> createParentChild(
  //     @Parameter(description = "Child DTO object", required = true) @RequestBody ChildDTO childDTO) {
  //   int numberOfRecordsCreated = parentChildInheritanceService.insertChildRecord(childDTO);
  //   GenericResponse genericResponse = GenericResponse.builder()
  //       .message("Number of Parent child " + numberOfRecordsCreated)
  //       .statusCode(HttpStatus.OK.name())
  //       .build();
  //   return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
  // }
}
