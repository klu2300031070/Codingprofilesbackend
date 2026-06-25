package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.leetcode.LeetCodeAnalysisReport;
import com.example.demo.service.leetcode.LeetCodeService;

import java.util.Map;

@RestController
@RequestMapping("/api/leetcode")
public class LeetCodeController {

    @Autowired
    private LeetCodeService leetCodeService;

    // Existing core profile mapping
    @GetMapping("/{username}")
    public Map<String, Object> getLeetCodeProfile(@PathVariable String username) {
        return leetCodeService.getUserProfile(username);
    }

    // Language statistics mapping
    @GetMapping("/{username}/languages")
    public Map<String, Object> getLanguageStats(@PathVariable String username) {
        return leetCodeService.getLanguageStatistics(username);
    }

    // Skill/Tag statistics mapping
    @GetMapping("/{username}/skills")
    public Map<String, Object> getSkillStats(@PathVariable String username) {
        return leetCodeService.getSkillStatistics(username);
    }

    // Contest and ranking history mapping
    @GetMapping("/{username}/contests")
    public Map<String, Object> getContestStats(@PathVariable String username) {
        return leetCodeService.getContestStatistics(username);
    }

    // 1. Problem Progress By Difficulty Mapping
    @GetMapping("/{username}/progress")
    public Map<String, Object> getProblemProgress(@PathVariable String username) {
        return leetCodeService.getProblemProgress(username);
    }

    // 2. Overall Submission Statistics Mapping
    @GetMapping("/{username}/submissions-stats")
    public Map<String, Object> getOverallSubmissionStats(@PathVariable String username) {
        return leetCodeService.getOverallSubmissionStats(username);
    }

    // 3. User Submission Calendar Mapping
    @GetMapping("/{username}/calendar")
    public Map<String, Object> getSubmissionCalendar(@PathVariable String username) {
        return leetCodeService.getSubmissionCalendar(username);
    }

    // 4. Recent Accepted Submissions Mapping (Defaults limit to 20 if not provided)
    @GetMapping("/{username}/recent-ac")
    public Map<String, Object> getRecentAcSubmissions(
            @PathVariable String username,
            @RequestParam(defaultValue = "20") int limit) {
        return leetCodeService.getRecentAcSubmissions(username, limit);
    }

    // 5. Active User Badge Information Mapping
    @GetMapping("/{username}/active-badge")
    public Map<String, Object> getActiveBadge(@PathVariable String username) {
        return leetCodeService.getActiveBadge(username);
    }
    @GetMapping("/{username}/ai-analysis")
    public LeetCodeAnalysisReport getAiAnalysis(@PathVariable String username) {
        return leetCodeService.generateUserAiAnalysis(username);
    }
}