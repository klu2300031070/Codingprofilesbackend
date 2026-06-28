package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.leetcode.LeetCodeAnalysisReport;
import com.example.demo.repo.UserRepo;
import com.example.demo.service.leetcode.LeetCodeService;

import java.util.Map;

@RestController
@RequestMapping("/api/leetcode")
public class LeetCodeController {

    @Autowired
    private LeetCodeService leetCodeService;
    
    @Autowired
    private UserRepo ur;

    // Existing core profile mapping
    @GetMapping("/")
    public Map<String, Object> getLeetCodeProfile() {
    	String username=SecurityContextHolder.getContext().getAuthentication().getName();
    	String lc=ur.findByUsername(username).getCodingProfile().getLcusername();
        return leetCodeService.getUserProfile(lc);
    }

    // Language statistics mapping
    @GetMapping("/languages")
    public Map<String, Object> getLanguageStats() {
    	String username=SecurityContextHolder.getContext().getAuthentication().getName();
    	String lc=ur.findByUsername(username).getCodingProfile().getLcusername();
        return leetCodeService.getLanguageStatistics(lc);
    }

    // Skill/Tag statistics mapping
    @GetMapping("/skills")
    public Map<String, Object> getSkillStats() {
    	String username=SecurityContextHolder.getContext().getAuthentication().getName();
    	String lc=ur.findByUsername(username).getCodingProfile().getLcusername();
        return leetCodeService.getSkillStatistics(lc);
    }

    // Contest and ranking history mapping
    @GetMapping("/contests")
    public Map<String, Object> getContestStats() {
    	String username=SecurityContextHolder.getContext().getAuthentication().getName();
    	String lc=ur.findByUsername(username).getCodingProfile().getLcusername();
        return leetCodeService.getContestStatistics(lc);
    }

    // 1. Problem Progress By Difficulty Mapping
    @GetMapping("/progress")
    public Map<String, Object> getProblemProgress() {
    	String username=SecurityContextHolder.getContext().getAuthentication().getName();
    	String lc=ur.findByUsername(username).getCodingProfile().getLcusername();
        return leetCodeService.getProblemProgress(lc);
    }

    // 2. Overall Submission Statistics Mapping
    @GetMapping("/submissions-stats")
    public Map<String, Object> getOverallSubmissionStats() {
    	String username=SecurityContextHolder.getContext().getAuthentication().getName();
    	String lc=ur.findByUsername(username).getCodingProfile().getLcusername();
        return leetCodeService.getOverallSubmissionStats(lc);
    }

    // 3. User Submission Calendar Mapping
    @GetMapping("/calendar")
    public Map<String, Object> getSubmissionCalendar() {
    	String username=SecurityContextHolder.getContext().getAuthentication().getName();
    	String lc=ur.findByUsername(username).getCodingProfile().getLcusername();
        return leetCodeService.getSubmissionCalendar(lc);
    }

    // 4. Recent Accepted Submissions Mapping (Defaults limit to 20 if not provided)
    @GetMapping("/recent-ac")
    public Map<String, Object> getRecentAcSubmissions(
            @PathVariable String username,
            @RequestParam(defaultValue = "20") int limit) {
        return leetCodeService.getRecentAcSubmissions(username, limit);
    }

    // 5. Active User Badge Information Mapping
    @GetMapping("/active-badge")
    public Map<String, Object> getActiveBadge() {
    	String username=SecurityContextHolder.getContext().getAuthentication().getName();
    	String lc=ur.findByUsername(username).getCodingProfile().getLcusername();
        return leetCodeService.getActiveBadge(lc);
    }
    @GetMapping("/ai-analysis")
    public LeetCodeAnalysisReport getAiAnalysis() {
    	String username=SecurityContextHolder.getContext().getAuthentication().getName();
    	String lc=ur.findByUsername(username).getCodingProfile().getLcusername();
        return leetCodeService.generateUserAiAnalysis(lc);
    }
}