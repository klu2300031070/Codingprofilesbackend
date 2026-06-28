package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.codeforces.UserAnalysisReport;
import com.example.demo.repo.UserRepo;
import com.example.demo.service.BrevoApiService;
import com.example.demo.service.codeforces.CodeforcesService;



@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private BrevoApiService brevoApiService;
    
    @Autowired
    private UserRepo ur;
    
    @Autowired
    private CodeforcesService cr;
    @GetMapping("/send-report")
    public ResponseEntity<String> triggerAnalysisEmail() {
    	String username=SecurityContextHolder.getContext().getAuthentication().getName();
    	String handle=ur.findByUsername(username).getCodingProfile().getCfusername();
    	String email=ur.findByUsername(username).getEmail();
        UserAnalysisReport data = cr.generateAdvancedAnalysis(handle);
        if (data == null || data.getTopicStats() == null) {
            return ResponseEntity
                    .status(400) // Bad Request
                    .body("Wrong username or user does not exist");
        }

        try {
            brevoApiService.sendWelcomeEmailViaApi(email, username, data);

            return ResponseEntity.ok(
                    "DSA analysis metrics processed.Send to " + email
            );

        } catch (Exception e) {
            return ResponseEntity
                    .status(500) // Internal Server Error
                    .body("Failed to send email");
        }
    }
}