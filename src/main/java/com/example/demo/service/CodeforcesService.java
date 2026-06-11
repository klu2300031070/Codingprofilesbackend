package com.example.demo.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable; // <-- Added Import

import org.springframework.core.ParameterizedTypeReference;

import org.springframework.http.HttpMethod;

import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;



import com.example.demo.dto.CodeforcesResponse;

import com.example.demo.dto.CodeforcesSubmission;

import com.example.demo.dto.CodeforcesSubmission.Problem;

import com.example.demo.dto.CodeforcesUserInfo;



import java.util.Collections;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import java.util.Map;



@Service

public class CodeforcesService {



    private final RestTemplate restTemplate;

    private final String BASE_URL = "https://codeforces.com/api/";
   


    public CodeforcesService() {

        this.restTemplate = new RestTemplate();

        


        this.restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {

            request.getHeaders().add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");

            return execution.execute(request, body);

        }));

    }




    @Cacheable(value = "codeforcesFullProfile", key = "#handle")

    public  Map<String, Object> getUserFullProfile(String handle) {

        System.out.println("⚠️ Cache MISS - Fetching Live Profile Data for: " + handle);
        Map<String, Object> fullData = new HashMap<>();
        // 1. Fetch User Info Profile Data
        String infoUrl = BASE_URL + "user.info?han=" + handle;
        try {
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
        // 2. Fetch User Submissions & Run Analytics Logic
        String statusUrl = BASE_URL + "user.status?handle=" + handle;
        try {
            ResponseEntity<CodeforcesResponse<List<CodeforcesSubmission>>> statusResponse = restTemplate.exchange(
                    statusUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<CodeforcesResponse<List<CodeforcesSubmission>>>() {}
            );
            if (statusResponse.getBody() != null && "OK".equals(statusResponse.getBody().getStatus())) {
                List<CodeforcesSubmission> submissions = statusResponse.getBody().getResult();
                fullData.put("submissions", submissions);
                fullData.put("Total submmisions", gettotalsubmmited(submissions));
                // Calculates performance across distinct topics covered
                //Map<String, Map<String, Object>> metrics = calculateTopicPerformance(submissions);
                //fullData.put("topicPerformance", metrics);
            }
        } catch (Exception e) {
            fullData.put("submissions_error", "Failed to fetch submissions: " + e.getMessage());
        }
        return fullData;
    }
    public static int  gettotalsubmmited(List<CodeforcesSubmission> ls) {
    	HashSet<String> hs=new HashSet<>();
    	for(CodeforcesSubmission i:ls) {
    		if(i.getVerdict().equals("OK")) {
    			hs.add(i.getProblem().getName());
    		}
    	}
    	return hs.size();
    }



    // Cache rule: Maps to "codeforcesTopicPerformance" bucket using handle as the key.

    // If not in Redis, runs the logic, creates the map, and pushes to Redis for 24 hours.

    @Cacheable(value = "codeforcesTopicPerformance", key = "#handle")
    public Map<String, Integer> calculateTopicPerformance(String handle) {
        System.out.println("⚠️ Cache MISS - Running Topic Analysis Analytics for: " + handle);
        Map<String, Integer> hm = new HashMap<>();

        // 1. Fetch User Info Profile Data
        String infoUrl = BASE_URL + "user.info?handles=" + handle;
        try {
            // We can completely ignore storing info inside fullData here since this method only returns 'hm'
            restTemplate.exchange(
                    infoUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<CodeforcesResponse<List<CodeforcesUserInfo>>>() {}
            );
        } catch (Exception e) {
            System.err.println("Failed to fetch profile info: " + e.getMessage());
        }

        // 2. Fetch User Submissions & Run Analytics Logic
        String statusUrl = BASE_URL + "user.status?handle=" + handle;
        try {
            ResponseEntity<CodeforcesResponse<List<CodeforcesSubmission>>> statusResponse = restTemplate.exchange(
                    statusUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<CodeforcesResponse<List<CodeforcesSubmission>>>() {}
            );

            if (statusResponse.getBody() != null && "OK".equals(statusResponse.getBody().getStatus())) {
                List<CodeforcesSubmission> submissions = statusResponse.getBody().getResult();
                
                if (submissions != null) {
                    for (CodeforcesSubmission i : submissions) {
                        // DEFENSIVE FIX: Guard against missing problem blocks or missing tag structures
                        if (i != null && i.getProblem() != null && i.getProblem().getTags() != null) {
                            for (String j : i.getProblem().getTags()) {
                                if (j != null) {
                                    hm.put(j, hm.getOrDefault(j, 0) + 1);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch or parse submissions: " + e.getMessage());
        }

        return hm;
    }

}