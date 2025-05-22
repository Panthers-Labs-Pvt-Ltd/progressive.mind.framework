package com.progressive.minds.chimera.examples.controller;


import com.progressive.minds.chimera.examples.model.generated.DataPipeline;
import com.progressive.minds.chimera.examples.model.generated.MetaDataPipeline;
import com.progressive.minds.chimera.examples.model.generated.UserProfile;
import com.progressive.minds.chimera.examples.services.ChimeraUserProfileService;
import java.util.List;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/examples")
public class ChimeraUserProfileController {

  @Autowired private ChimeraUserProfileService chimeraUserProfileService;
  
  @PostMapping("/profile")
  public ResponseEntity<String> createUserProfile(@RequestBody UserProfile userProfile) {
    chimeraUserProfileService.createUserProfile(userProfile);
    return new ResponseEntity<>("User Profile Created", HttpStatus.CREATED);
  }

  @GetMapping("/profiles")
  public ResponseEntity<List<UserProfile>> getUserProfile() {
    List<UserProfile> select = chimeraUserProfileService.getAllUserProfile();
    return new ResponseEntity<>(select, HttpStatus.OK);
  }

  @PostMapping("/pipeline")
  public ResponseEntity<String> createDataPipeline(@RequestBody DataPipeline dataPipeline) {
    chimeraUserProfileService.createDataPipeLine(dataPipeline);
    return new ResponseEntity<>("Data pipeline Created", HttpStatus.CREATED);
  }

  @PostMapping("/metadata/pipeline")
  public ResponseEntity<String> createMetaDataPipeline(@RequestBody MetaDataPipeline dataPipeline) {
    chimeraUserProfileService.createMetaDataPipeLine(dataPipeline);
    return new ResponseEntity<>("Data pipeline Created", HttpStatus.CREATED);
  }

@DeleteMapping("/metadata/delete/{id}")
public ResponseEntity<String> deleteMetaPipeline(@PathVariable("id") Long id) {
  chimeraUserProfileService.deleteFromMetaDataPipeline(id);
  return new ResponseEntity<>("DELETE", HttpStatus.OK);
}


  @DeleteMapping("/data/delete/{id}")
  public ResponseEntity<String> deleteDataPipeline(@PathVariable("id") Long id) {
    chimeraUserProfileService.deleteFromDataPipelineMapper(id);
    return new ResponseEntity<>("DELETE", HttpStatus.OK);
  }

  @DeleteMapping("/ppe/delete/{id}")
  public ResponseEntity<String> deletePipeline(@PathVariable("id") Long id) {
    chimeraUserProfileService.deletePipeline(id);
    return new ResponseEntity<>("DELETE", HttpStatus.OK);
  }

  @GetMapping("/pipelines")
  public ResponseEntity<List<DataPipeline>> getDataPipelines() {
    List<DataPipeline> pipelineList= chimeraUserProfileService.getAllDataPipeLines();
    return new ResponseEntity<>(pipelineList, HttpStatus.OK);
  }
  
}
