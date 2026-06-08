package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.LeetCodeProfileData;
import com.example.demo.service.LeetCodeService;

@RestController
@CrossOrigin(origins = "*")
 // Best practice for clean API path grouping
public class LeetCodeController {

    @Autowired
    public LeetCodeService leetCodeService;

    @GetMapping("/leetcode/{username}")
    public ResponseEntity<?> getLeetCodeData(@PathVariable String username) {
        // FIXED: Explicitly captures parsed LeetCodeProfileData object
        LeetCodeProfileData data = leetCodeService.getLeetCodeUserData(username);
        
        if (data == null) {
            return ResponseEntity.status(500).body("Failed to parse LeetCode metadata profiles.");
        }
        return ResponseEntity.ok(data); // Spring Boot converts this DTO into clean JSON in your browser automatically!
    }
}