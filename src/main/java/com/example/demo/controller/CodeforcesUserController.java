package com.example.demo.controller;


import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import com.example.demo.service.CodeforcesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class CodeforcesUserController {

    // Initialize Spring 3.x RestClient directly pointing to Codeforces API
    
    @Autowired
    private CodeforcesService cs;
  
    @GetMapping("/")
    public String getMethodName() {
        return "Project is working";
    }
    
    
    @GetMapping("/codeforces/{handle}")
    public ResponseEntity<Map<String, Object>> getCodeforceData(@PathVariable String handle) {
        Map<String, Object> data = cs.getUserFullProfile(handle);
        
        if (data.containsKey("profile_error") && data.containsKey("submissions_error")) {
            return ResponseEntity.status(500).body(data);
        }
        
        return ResponseEntity.ok(data);
    }
    @GetMapping("/codeforces/{handle}/gettopics")
    public ResponseEntity<Map<String, Integer>> getMethodName(@PathVariable String handle ) {
    	
        return ResponseEntity.ok(cs.calculateTopicPerformance(handle));
    }
    
}