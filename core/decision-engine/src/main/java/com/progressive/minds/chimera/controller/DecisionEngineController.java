package com.progressive.minds.chimera.controller;

import com.progressive.minds.chimera.mapper.UserProfileMapper;
import com.progressive.minds.chimera.model.UserProfile;
import java.util.List;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data/decision")
public class DecisionEngineController {

  @Autowired private UserProfileMapper userProfileMapper;

  @PostMapping
  public ResponseEntity<String> decision(@RequestBody UserProfile userProfile) {
    userProfileMapper.insert(userProfile);
    return new ResponseEntity<>("flink-job", HttpStatus.OK);
  }

  @GetMapping("/get")
  public ResponseEntity<List<UserProfile>> getDecision(@RequestBody UserProfile userProfile) {
    List<UserProfile> select = userProfileMapper.select(SelectDSLCompleter.allRows());
    return new ResponseEntity<>(select, HttpStatus.OK);
  }
}
