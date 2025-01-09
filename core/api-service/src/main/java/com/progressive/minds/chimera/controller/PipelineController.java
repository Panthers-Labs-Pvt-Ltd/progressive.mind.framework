package com.progressive.minds.chimera.controller;

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
        return pipelineService.getAllPipelines().stream()
                .filter(pipeline -> pipeline.getId() == id)
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // POST request - Add a new pipeline
    @PostMapping
    public ResponseEntity<String> createPipeline(@RequestBody DataPipeline pipeline) {
        pipelineService.insertPipeline(pipeline);
        return ResponseEntity.status(HttpStatus.CREATED).body("Pipeline created successfully with ID: " + pipeline.getId());
    }

    // GET request - Retrieve all pipelines
    @GetMapping
    public ResponseEntity<List<DataPipeline>> getAllPipelines() {
        return ResponseEntity.ok(pipelineService.getAllPipelines());
    }

    // PUT request - Update an existing pipeline by ID
    @PutMapping("/{id}")
    public ResponseEntity<String> updatePipeline(@PathVariable("id") int id, @RequestBody DataPipeline updatedPipeline) {
        pipelineService.updatePipeline(id, updatedPipeline);
        return ResponseEntity.status(HttpStatus.OK).body("Pipeline updated");
    }

}

