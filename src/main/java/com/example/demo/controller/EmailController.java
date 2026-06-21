package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserAnalysisReport;
import com.example.demo.service.BrevoApiService;
import com.example.demo.service.CodeforcesService;

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
    public String triggerAnalysisEmail(@PathVariable String email, @PathVariable String name,@PathVariable String handle) {
        
        
    	UserAnalysisReport data=cr.generateAdvancedAnalysis(handle);
    	
        // Execute updated SMTP delivery routine
        try {
			brevoApiService.sendWelcomeEmailViaApi(email, name, data);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return "DSA analysis metrics processed. Rich layout HTML template transmitted to " + email;
    }
}