package com.example.demo.service;

import org.jspecify.annotations.Nullable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.LeetCodeGraphQLResponse;
import com.example.demo.dto.LeetCodeProfileData;

import java.util.HashMap;
import java.util.Map;

@Service
public class LeetCodeService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String LEETCODE_GRAPHQL_URL = "https://leetcode.com/graphql";

    // Changed return type from String to LeetCodeProfileData to send structured data to browser
    public @Nullable LeetCodeProfileData getLeetCodeUserData(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        headers.set("Referer", "https://leetcode.com/");
        headers.set("Origin", "https://leetcode.com");

        String graphQLQuery = """
            query getUserData($username: String!) {
              matchedUser(username: $username) {
                tagProblemCounts {
                  fundamental { tagName tagSlug problemsSolved }
                  intermediate { tagName tagSlug problemsSolved }
                  advanced { tagName tagSlug problemsSolved }
                }
              }
              userContestRanking(username: $username) {
                attendedContestsCount
                rating
                globalRanking
              }
              userContestRankingHistory(username: $username) {
                attended
                ranking
                rating
                contest { title startTime }
              }
            }
            """;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", graphQLQuery);
        requestBody.put("variables", Map.of("username", username));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            // OPTIONAL: Print to console for verification WITHOUT breaking the code flow
            ResponseEntity<String> rawCheck = restTemplate.exchange(
                    LEETCODE_GRAPHQL_URL, HttpMethod.POST, entity, String.class
            );
            System.out.println("====== RAW LEETCODE API RESPONSE ======");
            System.out.println(rawCheck.getBody());
            System.out.println("=======================================");

            // FIXED: Map the execution cleanly directly into your DTO objects
            ResponseEntity<LeetCodeGraphQLResponse<LeetCodeProfileData>> response = restTemplate.exchange(
                    LEETCODE_GRAPHQL_URL,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<LeetCodeGraphQLResponse<LeetCodeProfileData>>() {}
            );

            if (response.getBody() != null) {
                return response.getBody().getData(); // Returns parsed object payload to the controller
            }
        } catch (Exception e) {
            System.err.println("Error executing communication layer request to LeetCode: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}