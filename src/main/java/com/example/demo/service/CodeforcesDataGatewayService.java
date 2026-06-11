package com.example.demo.service;

import java.util.Collections;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.dto.CodeforcesResponse;
import com.example.demo.dto.CodeforcesSubmission;

@Service
public class CodeforcesDataGatewayService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String BASE_URL = "https://codeforces.com/api/";

    public CodeforcesDataGatewayService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();

        this.restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {
            request.getHeaders().add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            return execution.execute(request, body);
        }));
    }

   
    @Cacheable(value = "codeforcesUserSubmissions", key = "#handle")
    public List<CodeforcesSubmission> getUserFullProfile(String handle) {
        System.out.println("⚠️ Cache MISS - Fetching Live Submissions Data for: " + handle);
        
       
        List<CodeforcesSubmission> submissions = Collections.emptyList(); 
        
       
        String statusUrl = BASE_URL + "user.status?handle=" + handle;
        
        try {
            
            String rawJsonText = restTemplate.getForObject(statusUrl, String.class);
            
            if (rawJsonText != null && rawJsonText.contains("\"status\":\"OK\"")) {
                
                CodeforcesResponse<List<CodeforcesSubmission>> statusResponse = objectMapper.readValue(
                        rawJsonText, 
                        new TypeReference<CodeforcesResponse<List<CodeforcesSubmission>>>() {}
                );
                
                if (statusResponse != null && statusResponse.getResult() != null) {
                    submissions = statusResponse.getResult();
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch or parse submissions: " + e.getMessage());
        }
        
        return submissions;
    }
}