package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.codeforces.UserAnalysisReport;
import com.example.demo.service.BrevoApiService;
import com.example.demo.service.codeforces.CodeforcesService;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private BrevoApiService brevoApiService;
    
    @Autowired
    private CodeforcesService cr;
    @GetMapping("/send-report/{email}/{name}/{handle}")
    public ResponseEntity<String> triggerAnalysisEmail(@PathVariable String email,@PathVariable String name,@PathVariable String handle) {
        UserAnalysisReport data = cr.generateAdvancedAnalysis(handle);
        if (data == null || data.getTopicStats() == null) {
            return ResponseEntity
                    .status(400) // Bad Request
                    .body("Wrong username or user does not exist");
        }

        try {
            brevoApiService.sendWelcomeEmailViaApi(email, name, data);

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