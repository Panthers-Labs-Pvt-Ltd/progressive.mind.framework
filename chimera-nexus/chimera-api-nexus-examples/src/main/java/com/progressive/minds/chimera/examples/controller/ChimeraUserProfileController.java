package com.progressive.minds.chimera.examples.controller;

import com.progressive.minds.chimera.examples.model.UserProfile;
import com.progressive.minds.chimera.examples.services.ChimeraUserProfileService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/examples")
public class ChimeraUserProfileController {

  @Autowired private ChimeraUserProfileService chimeraUserProfileService;
  
  @PostMapping
  public ResponseEntity<String> createUserProfile(@RequestBody UserProfile userProfile) {
    chimeraUserProfileService.createUserProfile(userProfile);
    return new ResponseEntity<>("User Profile Created", HttpStatus.CREATED);
  }

  @GetMapping("/profiles")
  public ResponseEntity<List<UserProfile>> getUserProfile() {
    List<UserProfile> select = chimeraUserProfileService.getAllUserProfile();
    return new ResponseEntity<>(select, HttpStatus.OK);
  }
  
}
