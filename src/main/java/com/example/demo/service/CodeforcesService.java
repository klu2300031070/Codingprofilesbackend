package com.example.demo.service;

import org.springframework.cache.annotation.Cacheable; // <-- Added back for annotation-driven caching
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.dto.CodeforcesResponse;
import com.example.demo.dto.CodeforcesSubmission;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CodeforcesService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String BASE_URL = "https://codeforces.com/api/";

    public CodeforcesService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        
        this.restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {
            request.getHeaders().add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            return execution.execute(request, body);
        }));
    }

    /**
     * CENTRAL RAW DATA GATEWAY
     * This annotation automatically handles checking Redis and pushing to Redis.
     * Every method or service that needs Codeforces data calls this method first.
     */
    @Cacheable(value = "codeforcesRawData", key = "#handle")
    public String getRawStatusData(String handle) {
        // This block ONLY executes on a Cache Miss (Redis doesn't have the handle)
        System.out.println("❌ [CACHE MISS] - Raw JSON text for '" + handle + "' not found in Redis.");
        System.out.println("🌐 [API REQUEST] - Requesting raw payload from Codeforces Live API...");
        
        String statusUrl = BASE_URL + "user.status?handle=" + handle;
        try {
            String rawJson = restTemplate.getForObject(statusUrl, String.class);
            
            if (rawJson != null && rawJson.contains("\"status\":\"OK\"")) {
                System.out.println("✅ [API SUCCESS] - Retrieved raw text. Spring will now push it to Redis.");
                return rawJson;
            }
        } catch (Exception e) {
            System.err.println("Codeforces API Call failed: " + e.getMessage());
        }
        return null;
    }

    /**
     * Method 1: Independent Profile Controller Destination
     */
    public Map<String, Object> getUserFullProfile(String handle) {
        Map<String, Object> fullData = new HashMap<>();
        
        // Hits the cached central gateway method
        String rawJson = getRawStatusData(handle);

        if (rawJson != null) {
            try {
                // Parse the raw text data locally on demand
                CodeforcesResponse<List<CodeforcesSubmission>> response = objectMapper.readValue(
                    rawJson, new TypeReference<CodeforcesResponse<List<CodeforcesSubmission>>>() {}
                );
                
                List<CodeforcesSubmission> submissions = response.getResult();
                fullData.put("submissions", submissions);
                fullData.put("Total submmisions", submissions.size());
            } catch (Exception e) {
                fullData.put("error", "Profile parsing failed: " + e.getMessage());
            }
        }
        return fullData;
    }

    /**
     * Method 2: Independent Analytics Controller Destination
     */
    public Map<String, Integer> calculateTopicPerformance(String handle) {
        Map<String, Integer> hm = new HashMap<>();
        
        // Hits the exact same cached central gateway method
        String rawJson = getRawStatusData(handle);

        if (rawJson != null) {
            try {
                // Parse the exact same raw text data locally on demand
                CodeforcesResponse<List<CodeforcesSubmission>> response = objectMapper.readValue(
                    rawJson, new TypeReference<CodeforcesResponse<List<CodeforcesSubmission>>>() {}
                );

                for (CodeforcesSubmission i : response.getResult()) {
                    if (i.getProblem() != null && i.getProblem().getTags() != null) {
                        for (String j : i.getProblem().getTags()) {
                            hm.put(j, hm.getOrDefault(j, 0) + 1);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Analytics parsing failed: " + e.getMessage());
            }
        }
        return hm;
    }
}