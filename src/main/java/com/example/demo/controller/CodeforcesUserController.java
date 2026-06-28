package com.example.demo.controller;


import java.util.*;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.codeforces.CodeforcesSubmission;
import com.example.demo.dto.codeforces.UserAnalysisReport;
import com.example.demo.repo.UserRepo;
import com.example.demo.service.codeforces.CodeforcesDataGatewayService;
import com.example.demo.service.codeforces.CodeforcesService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/codeforces")
public class CodeforcesUserController {

    
    
    @Autowired
    private CodeforcesService cs;
    
    @Autowired
    private CodeforcesDataGatewayService cds;
    
    @Autowired
    private UserRepo ur;
  
   
    
    
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getCodeforceData() {
    	String username=SecurityContextHolder.getContext().getAuthentication().getName();
    	String cf=ur.findByUsername(username).getCodingProfile().getCfusername();
    	Map<String, Object> data=cs.getUserFullProfile(cf);
        if (data.containsKey("profile_error") && data.containsKey("submissions_error")) {
            return ResponseEntity.status(500).body(data);
        }
        
        return ResponseEntity.ok(data);
    }
    
    @GetMapping("/gettopics")
    public ResponseEntity<Map<String, Integer>> getMethodName( ) {
    	String username=SecurityContextHolder.getContext().getAuthentication().getName();
    	String handle=ur.findByUsername(username).getCodingProfile().getCfusername();
        return ResponseEntity.ok(cs.calculateTopicPerformance(handle));
    }
    
    @GetMapping("/score")
    public UserAnalysisReport gettopicscores() {
    	String username=SecurityContextHolder.getContext().getAuthentication().getName();
    	String handle=ur.findByUsername(username).getCodingProfile().getCfusername();
        return cs.generateAdvancedAnalysis(handle);
    }
    
    
}