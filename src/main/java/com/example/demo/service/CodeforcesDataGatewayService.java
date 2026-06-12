package com.example.demo.service;

import java.util.Collections;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.CodeforcesResponse;
import com.example.demo.dto.CodeforcesSubmission;

@Service
public class CodeforcesDataGatewayService {

    private final RestTemplate restTemplate;
    private final String BASE_URL = "https://codeforces.com/api/";

    public CodeforcesDataGatewayService() {
        this.restTemplate = new RestTemplate();
        this.restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {
            request.getHeaders().add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            return execution.execute(request, body);
        }));
    }

    /**
     * 🟢 Returns exactly List<CodeforcesSubmission>
     * Caches the list object directly into Redis. Automatically expires at midnight tonight.
     */
    @Cacheable(value = "codeforcesFullProfile", key = "#handle")
    public List<CodeforcesSubmission> getUserFullProfile(String handle) {
        System.out.println("⚠️ Cache MISS - Fetching Live Submissions Data for: " + handle);
        String statusUrl = BASE_URL + "user.status?handle=" + handle;
        
        try {
            ResponseEntity<CodeforcesResponse<List<CodeforcesSubmission>>> statusResponse = restTemplate.exchange(
                    statusUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<CodeforcesResponse<List<CodeforcesSubmission>>>() {}
            );
            
            if (statusResponse.getBody() != null && "OK".equals(statusResponse.getBody().getStatus())) {
                return statusResponse.getBody().getResult();
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch submissions from Codeforces API: " + e.getMessage());
        }
        
        return Collections.emptyList(); // Safe fallback so application loops don't break
    }
}