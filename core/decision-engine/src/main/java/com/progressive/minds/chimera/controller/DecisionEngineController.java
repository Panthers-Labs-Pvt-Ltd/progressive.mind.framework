package com.progressive.minds.chimera.controller;

import com.progressive.minds.chimera.mapper.UserProfileMapper;
import com.progressive.minds.chimera.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data/decision")
public class DecisionEngineController {

  @Autowired private UserProfileMapper userProfileMapper;

  @PostMapping
  public ResponseEntity<String> decision(@RequestBody UserProfile userProfile) {
    userProfileMapper.insert(userProfile);
        return new ResponseEntity<>("flink-job", HttpStatus.OK);
    }
}
