package com.example.demo.service.leetcode;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.leetcode.LeetCodeAnalysisReport;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LeetCodeService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String LEETCODE_GRAPHQL_URL = "https://leetcode.com/graphql/";

    // Helper method to eliminate duplicate network request boilerplates
    private Map<String, Object> executeGraphQLQuery(String query, Map<String, Object> variables, String operationName) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            // Setting a standard User-Agent prevents upstream 403 Forbidden blockers from LeetCode
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("query", query);
            requestBody.put("variables", variables);
            requestBody.put("operationName", operationName);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            return restTemplate.postForObject(LEETCODE_GRAPHQL_URL, entity, Map.class);
        } catch (Exception e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Failed to retrieve data from LeetCode upstream API");
            errorMap.put("message", e.getMessage());
            return errorMap;
        }
    }

    // Existing core user profile execution logic
    public Map<String, Object> getUserProfile(String username) {
        String query = """
            query userProfile($username: String!) {
              matchedUser(username: $username) {
                username
                submitStatsGlobal {
                  acSubmissionNum {
                    difficulty
                    count
                  }
                }
              }
            }
            """;
        Map<String, Object> variables = Map.of("username", username);
        return executeGraphQLQuery(query, variables, "userProfile");
    }

    // Language stats service logic
    public Map<String, Object> getLanguageStatistics(String username) {
        String query = """
            query languageStats($username: String!) {
              matchedUser(username: $username) {
                languageProblemCount { languageName problemsSolved }
              }
            }
            """;
        Map<String, Object> variables = Map.of("username", username);
        return executeGraphQLQuery(query, variables, "languageStats");
    }

    // Skill stats service logic
    public Map<String, Object> getSkillStatistics(String username) {
        String query = """
            query skillStats($username: String!) {
              matchedUser(username: $username) {
                tagProblemCounts {
                  advanced { tagName tagSlug problemsSolved }
                  intermediate { tagName tagSlug problemsSolved }
                  fundamental { tagName tagSlug problemsSolved }
                }
              }
            }
            """;
        Map<String, Object> variables = Map.of("username", username);
        return executeGraphQLQuery(query, variables, "skillStats");
    }

    // Contest History and Rankings service logic
    public Map<String, Object> getContestStatistics(String username) {
        String query = """
            query userContestRankingInfo($username: String!) {
              userContestRanking(username: $username) {
                attendedContestsCount rating globalRanking totalParticipants
                topPercentage badge { name }
              }
              userContestRankingHistory(username: $username) {
                attended trendDirection problemsSolved totalProblems
                finishTimeInSeconds rating ranking
                contest { title startTime }
              }
            }
            """;
        Map<String, Object> variables = Map.of("username", username);
        return executeGraphQLQuery(query, variables, "userContestRankingInfo");
    }

    // 1. Problem Progress By Difficulty
    public Map<String, Object> getProblemProgress(String username) {
        String query = """
            query userProfileUserQuestionProgressV2($userSlug: String!) {
              userProfileUserQuestionProgressV2(userSlug: $userSlug) {
                numAcceptedQuestions { difficulty count }
                numFailedQuestions { difficulty count }
                numUntouchedQuestions { difficulty count }
              }
            }
            """;
        Map<String, Object> variables = Map.of("userSlug", username);
        return executeGraphQLQuery(query, variables, "userProfileUserQuestionProgressV2");
    }

    // 2. Overall Submission Statistics
    public Map<String, Object> getOverallSubmissionStats(String username) {
        String query = """
            query userProblemsSolved($username: String!) {
              allQuestionsCount { difficulty count }
              matchedUser(username: $username) {
                submitStats {
                  acSubmissionNum { difficulty count submissions }
                  totalSubmissionNum { difficulty count submissions }
                }
              }
            }
            """;
        Map<String, Object> variables = Map.of("username", username);
        return executeGraphQLQuery(query, variables, "userProblemsSolved");
    }

    // 3. User Submission Calendar
    public Map<String, Object> getSubmissionCalendar(String username) {
        String query = """
            query userProfileCalendar($username: String!) {
              matchedUser(username: $username) {
                userCalendar {
                  activeYears
                  streak
                  totalActiveDays
                  submissionCalendar
                }
              }
            }
            """;
        Map<String, Object> variables = Map.of("username", username);
        return executeGraphQLQuery(query, variables, "userProfileCalendar");
    }

    // 4. Recent Accepted Submissions
    public Map<String, Object> getRecentAcSubmissions(String username, int limit) {
        String query = """
            query recentAcSubmissions($username: String!, $limit: Int!) {
              recentAcSubmissionList(username: $username, limit: $limit) {
                id title titleSlug timestamp
              }
            }
            """;
        Map<String, Object> variables = Map.of("username", username, "limit", limit);
        return executeGraphQLQuery(query, variables, "recentAcSubmissions");
    }

    // 5. Active User Badge Information
    public Map<String, Object> getActiveBadge(String username) {
        String query = """
            query getUserProfile($username: String!) {
              matchedUser(username: $username) {
                activeBadge { displayName icon }
              }
            }
            """;
        Map<String, Object> variables = Map.of("username", username);
        return executeGraphQLQuery(query, variables, "getUserProfile");
    }
    
   // private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${gemini.api.key}")
    private String geminiApiKey;


    public LeetCodeAnalysisReport generateUserAiAnalysis(String username) {
        Map<String, Object> skillStats = getSkillStatistics(username);
        Map<String, Object> progressStats = getProblemProgress(username);
        Map<String, Object> submissionStats = getOverallSubmissionStats(username);

        String systemPrompt = """
        	    You are an expert technical evaluation machine. Map the incoming user's LeetCode performance details against the fundamental Data Structures and Algorithms (DSA) linear dependency hierarchy.
        	    
        	    DSA HIERARCHY REFERENCE RULE:
        	    Basic Building Blocks (Arrays, Strings) -> Linear Structures (Linked Lists, Stacks, Queues) -> Non-Linear Complex Nodes (Trees, Heaps, Tries) -> Advanced Relational Architectures (Graphs, Backtracking, Dynamic Programming).
        	    
        	    CRITICAL INPUT FILTERING LOGIC:
        	    - Review the user's dataset carefully. You must NEVER return null or an empty array [] for 'suggestedProblems' under any tag in 'tagPerformances'.
        	    - EVERY tag included in 'tagPerformances' MUST have a MINIMUM of 3 distinct, highly relevant practice problems suggested.
        	    - Tailor the problem difficulties to their 'score' and 'masteryLevel':
        	      * If a tag has a high score (7 to 10) and masteryLevel is "Pro", DO NOT suggest entry-level questions they have already solved. Instead, suggest 3 highly complex, advanced optimization or "Hard" level variations to challenge them.
        	      * If a tag has a mid score (4 to 6) and masteryLevel is "Intermediate", suggest 3 solid interview-classic "Medium" variations.
        	      * If a tag has a low score (0 to 3) and masteryLevel is "Beginner", suggest 3 foundational "Easy" or introductory "Medium" questions.
        	    
        	    CRITICAL OUTPUT HIERARCHY REQUIREMENTS:
        	    1. 'overallSuggestedProblems' MUST contain a minimum of 10 distinct problems, ordered linearly using 'stepOrder' (from 1 to 10+) following a clear dependency track of weak topics.
        	    2. Each problem inside both tag-specific and overall blocks must link to a valid URL string matching: "https://leetcode.com/problems/problem-title-slug/".
        	    3. 'overallDsaScore' must be a normalized rating between 0 and 10.
        	    4. Output the valid JSON structure directly without markdown code fences.
        	    
        	    Target Output JSON Blueprint Structure:
        	    {
        	      "username": "string",
        	      "overallDsaScore": int,
        	      "totalProblemsSolved": int,
        	      "executiveSummary": "string",
        	      "currentDsaHierarchyStage": "string",
        	      "nextRecommendedTopicToLearn": "string",
        	      "tagPerformances": [
        	        {
        	          "tagName": "string",
        	          "masteryLevel": "string",
        	          "score": int,
        	          "analysis": "string",
        	          "suggestedProblems": [
        	            {
        	              "stepOrder": int,
        	              "problemTitle": "string",
        	              "difficulty": "string",
        	              "leetCodeUrl": "string",
        	              "coreReasoning": "string"
        	            }
        	          ]
        	        }
        	      ],
        	      "overallSuggestedProblems": [
        	        {
        	          "stepOrder": int,
        	          "problemTitle": "string",
        	          "difficulty": "string",
        	          "leetCodeUrl": "string",
        	          "coreReasoning": "string"
        	        }
        	      ],
        	      "globalActionPlanRoadmap": ["string"]
        	    }
        	    
        	    User Data Analytics Context:
        	    """;

        String userContext = String.format("""
            Username Target: %s
            Topic Stats: %s
            Completion Statuses: %s
            Overall Actions: %s
            """, username, skillStats.toString(), progressStats.toString(), submissionStats.toString());

        String finalPrompt = systemPrompt + userContext;
        String geminiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + geminiApiKey;        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> textPart = Map.of("text", finalPrompt);
            Map<String, Object> partsContainer = Map.of("parts", List.of(textPart));
            Map<String, Object> responseMimeType = Map.of("responseMimeType", "application/json");
            
            Map<String, Object> requestPayload = new HashMap<>();
            requestPayload.put("contents", List.of(partsContainer));
            requestPayload.put("generationConfig", responseMimeType);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestPayload, headers);
            Map<String, Object> geminiRawResponse = restTemplate.postForObject(geminiUrl, entity, Map.class);

            String rawJsonText = extractGeminiText(geminiRawResponse);
            return objectMapper.readValue(rawJsonText, LeetCodeAnalysisReport.class);

        } catch (Exception e) {
            LeetCodeAnalysisReport errorReport = new LeetCodeAnalysisReport();
            errorReport.setUsername(username);
            errorReport.setExecutiveSummary("Failed to parse hierarchy roadmap data: " + e.getMessage());
            return errorReport;
        }
    }

    private String extractGeminiText(Map<String, Object> response) {
        try {
            List<?> candidates = (List<?>) response.get("candidates");
            Map<?, ?> firstCandidate = (Map<?, ?>) candidates.get(0);
            Map<?, ?> content = (Map<?, ?>) firstCandidate.get("content");
            List<?> parts = (List<?>) content.get("parts");
            Map<?, ?> firstPart = (Map<?, ?>) parts.get(0);
            return (String) firstPart.get("text");
        } catch (Exception e) {
            throw new RuntimeException("Error reading internal token mapping tree from Gemini payload.", e);
        }
    }
}