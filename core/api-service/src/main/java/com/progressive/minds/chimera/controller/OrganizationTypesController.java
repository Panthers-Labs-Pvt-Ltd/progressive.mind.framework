package com.progressive.minds.chimera.controller;

import com.progressive.minds.chimera.common.dto.GenericResponse;
import com.progressive.minds.chimera.dto.OrganizationTypes;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import com.progressive.minds.chimera.service.OrganizationTypesService;
import java.util.List;

import javax.annotation.CheckForNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/organizationTypes")
public class OrganizationTypesController {

    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(DataSourcesConnectionsController.class);

    private final OrganizationTypesService orgType;

    @Autowired
    public OrganizationTypesController(OrganizationTypesService orgType) {
        this.orgType = orgType;
    }

    // GET request - Retrieve a data source connection by connectionId
    @CheckForNull
    @GetMapping("/{orgName}")
    public ResponseEntity<OrganizationTypes> getConnectionById(@PathVariable("orgName") String orgName) {
        logger.logInfo("Fetching Org with Name: " + orgName);
        return ResponseEntity.ok(orgType.getOrganizationByName(orgName).orElse(null));
    }

    // POST request - Add a new data source connection
    @PostMapping("/create")
    public ResponseEntity<GenericResponse> createOrganization(@RequestBody OrganizationTypes org) {
        int createdRecords = orgType.insertOrganization(org);
        if (createdRecords == 0) {
            GenericResponse response = GenericResponse.builder()
                    .message("Failed to create the organization. Please try again.")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        GenericResponse response = GenericResponse.builder()
                .message("Organization created successfully with Name: " + org.getOrgTypeName())
                .statusCode(HttpStatus.CREATED.name())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET request - Retrieve all data source connections
    @GetMapping
    public ResponseEntity<List<OrganizationTypes>> getAllOrganizations() {
        logger.logInfo("Fetching all Organizations.");
        return ResponseEntity.ok(orgType.getAllOrganizations());
    }

    // PUT request - Update an existing data source connection
    @PutMapping("/update")
    public ResponseEntity<GenericResponse> updateConnection(@RequestBody OrganizationTypes updatedOrg) {
        String orgName = updatedOrg.getOrgTypeName();
        if (orgType.getOrganizationByName(orgName).isEmpty()) {
            GenericResponse response = GenericResponse.builder()
                    .message("Org doesn't exist with the given Name: " + orgName)
                    .statusCode(HttpStatus.NOT_FOUND.name())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        int updatedRows = orgType.updateOrganization(updatedOrg);
        if (updatedRows == 0) {
            GenericResponse response = GenericResponse.builder()
                    .message("Failed to update the Org with NAme: " + orgName)
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        GenericResponse response = GenericResponse.builder()
                .message("Org updated successfully with Name: " + orgName)
                .statusCode(HttpStatus.OK.name())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // DELETE request - Delete a data source connection by ID
    @DeleteMapping("/delete/{orgName}")
    public ResponseEntity<GenericResponse> deleteConnection(@PathVariable("orgName") String orgName) {
        int deletedRows = orgType.deleteConnection(orgName);
        if (deletedRows == 0) {
            GenericResponse response = GenericResponse.builder()
                    .message("Org doesn't exist with the given Name: " + orgName)
                    .statusCode(HttpStatus.NOT_FOUND.name())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        GenericResponse response = GenericResponse.builder()
                .message("Org with Name: " + orgName + " deleted successfully.")
                .statusCode(HttpStatus.OK.name())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // GET request - Count total connections
    @GetMapping("/count")
    public ResponseEntity<GenericResponse> countConnections() {
        long totalConnections = orgType.getTotalNumberOfOrganizations();
        GenericResponse response = GenericResponse.builder()
                .message("Total number of Orgs: " + totalConnections)
                .statusCode(HttpStatus.OK.name())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
