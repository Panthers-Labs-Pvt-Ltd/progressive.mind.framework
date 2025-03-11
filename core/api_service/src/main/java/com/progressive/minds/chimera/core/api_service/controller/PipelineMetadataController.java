package com.progressive.minds.chimera.core.api_service.controller;

import com.progressive.minds.chimera.core.api_service.dto.PipelineMetadata;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import com.progressive.minds.chimera.core.api_service.service.PipelineMetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pipelineMetadata")
public class PipelineMetadataController {

    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(PipelineController.class);

    private PipelineMetadataService pipelineMedataService;

    @Autowired
    public PipelineMetadataController(PipelineMetadataService pipelineMedataService) {
        this.pipelineMedataService = pipelineMedataService;
    }

    // GET request - Retrieve an existing config by pipeline name
    @GetMapping("/{name}")
    public ResponseEntity<PipelineMetadata> getPipelineMetadata(@PathVariable("name") String name) {
        logger.logInfo("Fetching Pipeline Metadata for pipeline: " + name + " from the database.");
        return ResponseEntity.ok(pipelineMedataService.getPipelineMetadata(name));
    }
}
