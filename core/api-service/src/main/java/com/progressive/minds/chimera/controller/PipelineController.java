package com.progressive.minds.chimera.controller;

import com.progressive.minds.chimera.common.dto.GenericResponse;
import com.progressive.minds.chimera.entity.DataPipeline;
import com.progressive.minds.chimera.service.PipelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pipelines")
public class PipelineController {

    private PipelineService pipelineService;

    @Autowired
    public PipelineController(PipelineService pipelineService) {
        this.pipelineService = pipelineService;
    }

    // GET request - Retrieve an existing pipeline by ID
    @GetMapping("/{id}")
    public ResponseEntity<DataPipeline> getPipelineById(@PathVariable int id) {
        return ResponseEntity.ok(pipelineService.getDataPipeLineById(id));
    }

    // POST request - Add a new pipeline
    @PostMapping
    public ResponseEntity<GenericResponse> createPipeline(@RequestBody DataPipeline pipeline) {
        pipelineService.insertPipeline(pipeline);
        GenericResponse genericResponse = GenericResponse.builder()
                .message("Pipeline created successfully with ID: " + pipeline.getId())
                .statusCode(HttpStatus.CREATED.name())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(genericResponse);
    }

    // GET request - Retrieve all pipelines
    @GetMapping
    public ResponseEntity<List<DataPipeline>> getAllPipelines() {
        return ResponseEntity.ok(pipelineService.getAllPipelines());
    }

    // PUT request - Update an existing pipeline by ID
    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse> updatePipeline(@PathVariable("id") int id, @RequestBody DataPipeline updatedPipeline) {
        if (pipelineService.isDataPipeLineExists(id)) {
            pipelineService.updatePipeline(id, updatedPipeline);
            GenericResponse genericResponse = GenericResponse.builder()
                    .message("Pipeline updated successfully with ID: " + id)
                    .statusCode(HttpStatus.NO_CONTENT.name())
                    .build();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(genericResponse);
        } else {
            GenericResponse genericResponse = GenericResponse.builder()
                    .message("Pipeline don't exist with the given Id: " + id)
                    .statusCode(HttpStatus.NOT_FOUND.name())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }
    }

}

