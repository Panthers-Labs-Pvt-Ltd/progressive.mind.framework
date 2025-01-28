package com.progressive.minds.chimera.controller;

import com.progressive.minds.chimera.common.dto.GenericResponse;
import com.progressive.minds.chimera.dto.OrganizationHierarchy;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import com.progressive.minds.chimera.service.OrganizationHierarchyService;
import java.util.List;

import javax.annotation.CheckForNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/organizationHierarchy")
public class OrganizationHierarchyController {

    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(DataSourcesConnectionsController.class);

    private final OrganizationHierarchyService orgHier;

    @Autowired
    public OrganizationHierarchyController(OrganizationHierarchyService orgHier) {
        this.orgHier = orgHier;
    }

    // GET request - Retrieve a data source connection by connectionId
    @CheckForNull
    @GetMapping("/{orgName}")
    public ResponseEntity<OrganizationHierarchy> getOrgHierByName(@PathVariable("orgName") String orgName) {
        logger.logInfo("Fetching Org Hier with Name: " + orgName);
        return ResponseEntity.ok(orgHier.getOrgHierarchyByName(orgName).orElse(null));
    }

    // POST request - Add a new data source connection
    @PostMapping("/create")
    public ResponseEntity<GenericResponse> createOrganization(@RequestBody OrganizationHierarchy org) {
        int createdRecords = orgHier.insertOrgHier(org);
        if (createdRecords == 0) {
            GenericResponse response = GenericResponse.builder()
                    .message("Failed to create the organization Hierarchy. Please try again.")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        GenericResponse response = GenericResponse.builder()
                .message("Organization Hierarchy created successfully with Name: " + org.getOrgTypeName())
                .statusCode(HttpStatus.CREATED.name())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET request - Retrieve all data source connections
    @GetMapping
    public ResponseEntity<List<OrganizationHierarchy>> getAllOrganizationHierarchy() {
        logger.logInfo("Fetching all Organization Hierarchy.");
        return ResponseEntity.ok(orgHier.getAllOrgHierarchy());
    }

    // PUT request - Update an existing data source connection
    @PutMapping("/update")
    public ResponseEntity<GenericResponse> updateConnection(@RequestBody OrganizationHierarchy updatedOrg) {
        String orgName = updatedOrg.getOrgHierName();
        if (orgHier.getOrgHierarchyByName(orgName).isEmpty()) {
            GenericResponse response = GenericResponse.builder()
                    .message("Org Hierarchy doesn't exist with the given Name: " + orgName)
                    .statusCode(HttpStatus.NOT_FOUND.name())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        int updatedRows = orgHier.updateOrgHier(updatedOrg);
        if (updatedRows == 0) {
            GenericResponse response = GenericResponse.builder()
                    .message("Failed to update the Org Hierarchy with NAme: " + orgName)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        GenericResponse response = GenericResponse.builder()
                .message("Org Hierarchy updated successfully with Name: " + orgName)
                .statusCode(HttpStatus.OK.name())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // DELETE request - Delete a data source connection by ID
    @DeleteMapping("/delete/{orgName}")
    public ResponseEntity<GenericResponse> deleteOrgHierarchy(@PathVariable("orgName") String orgName) {
        int deletedRows = orgHier.deleteOrgHier(orgName);
        if (deletedRows == 0) {
            GenericResponse response = GenericResponse.builder()
                    .message("Org Hierarchy doesn't exist with the given Name: " + orgName)
                    .statusCode(HttpStatus.NOT_FOUND.name())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        GenericResponse response = GenericResponse.builder()
                .message("Org Hierarchy with Name: " + orgName + " deleted successfully.")
                .statusCode(HttpStatus.OK.name())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // GET request - Count total connections
    @GetMapping("/count")
    public ResponseEntity<GenericResponse> countConnections() {
        long totalConnections = orgHier.getTotalNumberOfOrgHierarchy();
        GenericResponse response = GenericResponse.builder()
                .message("Total number of Orgs: " + totalConnections)
                .statusCode(HttpStatus.OK.name())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
