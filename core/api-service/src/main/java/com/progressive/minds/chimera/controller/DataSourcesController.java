package com.progressive.minds.chimera.controller;

import com.progressive.minds.chimera.common.dto.GenericResponse;
import com.progressive.minds.chimera.dto.DataSources;
import com.progressive.minds.chimera.foundational.logging.ChimeraLogger;
import com.progressive.minds.chimera.foundational.logging.ChimeraLoggerFactory;
import com.progressive.minds.chimera.service.dataSourcesService;
import java.util.List;

import javax.annotation.CheckForNull;

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
@RequestMapping("/api/v1/dataSources")
public class DataSourcesController {

    private static final ChimeraLogger logger = ChimeraLoggerFactory.getLogger(PipelineController.class);

    private dataSourcesService dataSourcesService;

    @Autowired
    public DataSourcesController(dataSourcesService dataSourcesService) {
        this.dataSourcesService = dataSourcesService;
    }

    // GET request - Retrieve an existing data source by sourceType and sourceSubType
    @CheckForNull
    @GetMapping("/{sourceType}/{sourceSubType}")
    public ResponseEntity<DataSources> getDataSourcesByTypeAndSubtype(@PathVariable("sourceType") String sourceType, @PathVariable("sourceSubType") String sourceSubType) {
        logger.logInfo("Fetching data source with type: " + sourceType + " and subtyp: " + sourceSubType + " from the database.");
        return ResponseEntity.ok(dataSourcesService.getDataSourceByTypeAndSubtype(sourceType, sourceSubType));
    }

    // POST request - Add a new dataSource
    @PostMapping("/create")
    public ResponseEntity<GenericResponse> createPipeline(@RequestBody DataSources dataSource) {
        int numberOfRecordsCreated = dataSourcesService.insertDataSource(dataSource);
        if (numberOfRecordsCreated == 0) {
            GenericResponse genericResponse = GenericResponse.builder()
            .message("Failed to create the Data Source. Please try again.")
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
            .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genericResponse);
        }

        GenericResponse genericResponse = GenericResponse.builder()
        .message("Data Source created successfully with data source type" + dataSource.getDataSourceType() + " and data source sub type : " + dataSource.getDataSourceSubType())
        .statusCode(HttpStatus.CREATED.name())
        .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(genericResponse);
    }

    // GET request - Retrieve all data sources
    @GetMapping
    public ResponseEntity<List<DataSources>> getAllDataSources() {
        return ResponseEntity.ok(dataSourcesService.getAllDataSources());
    }

    // PUT request - Update an existing data source by data source name and data source sub type
    @PutMapping("/update")
    public ResponseEntity<GenericResponse> updatePipeline(@RequestBody DataSources updatedDataSource) {
        String dataSourceType = updatedDataSource.getDataSourceType();
        String dataSourceSubType = updatedDataSource.getDataSourceSubType();

        if (dataSourcesService.getDataSourceByTypeAndSubtype(dataSourceType, dataSourceSubType) == null) {
        // data Source does not exist
        GenericResponse genericResponse = GenericResponse.builder()
          .message("Data Source doesn't exist with the given type: " + dataSourceType + " and sub type: " + dataSourceSubType)
          .statusCode(HttpStatus.NOT_FOUND.name())
          .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
        }

        int updatedRows = dataSourcesService.updateDataSource(updatedDataSource);
        if (updatedRows == 0) {
        // Update operation didn't affect any rows
        GenericResponse genericResponse = GenericResponse.builder()
          .message("Data Source update failed for type: " + dataSourceType + " and sub type: " + dataSourceSubType)
          .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
          .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genericResponse);
        }

        // Update operation succeeded
        GenericResponse genericResponse = GenericResponse.builder()
        .message("Data Source updated successfully with type: " + dataSourceType + " and sub type: " + dataSourceSubType)
        .statusCode(HttpStatus.OK.name())
        .build();
        return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
    }

  // Delete request -delete pipeline
  @DeleteMapping("/delete/{dataSourceType}/{dataSourceSubType}")
  public ResponseEntity<GenericResponse> deletePipeline(@PathVariable("dataSourceType") String dataSourceType, @PathVariable("dataSourceSubType") String dataSourceSubType) {
    if (dataSourcesService.deleteDataSource(dataSourceType, dataSourceSubType) == 0) {
      GenericResponse genericResponse = GenericResponse.builder()
          .message("Data Source doesn't exist with the given type: " + dataSourceType + " and sub type: " + dataSourceSubType)
          .statusCode(HttpStatus.NOT_FOUND.name())
          .build();
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genericResponse);
    } else {
      GenericResponse genericResponse = GenericResponse.builder()
          .message("Data Source with type : " + dataSourceType + " and sub type : " + dataSourceSubType + "deleted successfully")
          .statusCode(HttpStatus.OK.name())
          .build();
      return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
    }
  }

  // Delete request -delete pipeline
  @GetMapping("/count")
  public ResponseEntity<GenericResponse> countNumberOfDataSources() {
    long totalNumberOfPipeline = dataSourcesService.getTotalNumberOfDataSources();
    GenericResponse genericResponse = GenericResponse.builder()
        .message("Number of Data data sources " + totalNumberOfPipeline)
        .statusCode(HttpStatus.OK.name())
        .build();
    return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
  }

}

