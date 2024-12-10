package com.progressive.minds.chimera.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data/decision")
public class DecisionEngineController {
    @PostMapping
    public ResponseEntity<String> decision(@RequestBody String data) {
        return new ResponseEntity<>("flink-job", HttpStatus.OK);
    }
}
