package com.example.demo.controller;


import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.UserAnalysisReport;
import com.example.demo.service.CodeforcesDataGatewayService;
import com.example.demo.service.CodeforcesService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
public class CodeforcesUserController {

    
    
    @Autowired
    private CodeforcesService cs;
    
    @Autowired
    private CodeforcesDataGatewayService cds;
  
    @GetMapping("/")
    public String getMethodName(HttpServletRequest hq) {
        return "Project is working " +hq.getSession().getId();
    }
    
    
    @GetMapping("/codeforces/{handle}")
    public ResponseEntity<Map<String, Object>> getCodeforceData(@PathVariable String handle) {
    	
    	Map<String, Object> data=cs.getUserFullProfile(handle);
        if (data.containsKey("profile_error") && data.containsKey("submissions_error")) {
            return ResponseEntity.status(500).body(data);
        }
        
        return ResponseEntity.ok(data);
    }
    @GetMapping("/codeforces/{handle}/gettopics")
    public ResponseEntity<Map<String, Integer>> getMethodName(@PathVariable String handle ) {
    	
        return ResponseEntity.ok(cs.calculateTopicPerformance(handle));
    }
    
    @GetMapping("/codeforces/{handle}/score")
    public UserAnalysisReport gettopicscores(@PathVariable String handle) {
        return cs.generateAdvancedAnalysis(handle);
    }
    
    
}