package com.example.demo.service;


import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.CodeforcesResponse;
import com.example.demo.dto.CodeforcesSubmission;
import com.example.demo.dto.CodeforcesUserInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CodeforcesService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "https://codeforces.com/api/";

    public Map<String, Object> getUserFullProfile(String handle) {
        Map<String, Object> fullData = new HashMap<>();

        // 1. Fetch User Info
        String infoUrl = BASE_URL + "user.info?handles=" + handle;
        try {
            // Using ParameterizedTypeReference due to Java generic erasure in Jackson
            ResponseEntity<CodeforcesResponse<List<CodeforcesUserInfo>>> infoResponse = restTemplate.exchange(
                    infoUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<CodeforcesResponse<List<CodeforcesUserInfo>>>() {}
            );
            
            if (infoResponse.getBody() != null && "OK".equals(infoResponse.getBody().getStatus())) {
                fullData.put("profile", infoResponse.getBody().getResult().get(0));
            }
        } catch (Exception e) {
            fullData.put("profile_error", "Failed to fetch profile: " + e.getMessage());
        }

        // 2. Fetch User Submissions (History + Concept Tags)
        String statusUrl = BASE_URL + "user.status?handle=" + handle;
        try {
            ResponseEntity<CodeforcesResponse<List<CodeforcesSubmission>>> statusResponse = restTemplate.exchange(
                    statusUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<CodeforcesResponse<List<CodeforcesSubmission>>>() {}
            );

            if (statusResponse.getBody() != null && "OK".equals(statusResponse.getBody().getStatus())) {
                fullData.put("submissions", statusResponse.getBody().getResult());
                fullData.put("Total submmisions",statusResponse.getBody().getResult().size());
            }
        } catch (Exception e) {
            fullData.put("submissions_error", "Failed to fetch submissions: " + e.getMessage());
        }

        return fullData;
    }
}