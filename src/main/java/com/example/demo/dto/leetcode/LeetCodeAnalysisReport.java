package com.example.demo.dto.leetcode;

import java.util.List;

public class LeetCodeAnalysisReport {
    private String username;
    private int overallDsaScore;              // Strictly 0 to 10 rating
    private int totalProblemsSolved;          // Where the '301' tally safely belongs
    private String executiveSummary;
    private String currentDsaHierarchyStage;
    private String nextRecommendedTopicToLearn;
    private List<TagPerformance> tagPerformances;
    private List<SuggestedProblem> overallSuggestedProblems;
    private List<String> globalActionPlanRoadmap;

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getOverallDsaScore() { return overallDsaScore; }
    public void setOverallDsaScore(int overallDsaScore) { this.overallDsaScore = overallDsaScore; }

    public int getTotalProblemsSolved() { return totalProblemsSolved; }
    public void setTotalProblemsSolved(int totalProblemsSolved) { this.totalProblemsSolved = totalProblemsSolved; }

    public String getExecutiveSummary() { return executiveSummary; }
    public void setExecutiveSummary(String executiveSummary) { this.executiveSummary = executiveSummary; }

    public String getCurrentDsaHierarchyStage() { return currentDsaHierarchyStage; }
    public void setCurrentDsaHierarchyStage(String currentDsaHierarchyStage) { this.currentDsaHierarchyStage = currentDsaHierarchyStage; }

    public String getNextRecommendedTopicToLearn() { return nextRecommendedTopicToLearn; }
    public void setNextRecommendedTopicToLearn(String nextRecommendedTopicToLearn) { this.nextRecommendedTopicToLearn = nextRecommendedTopicToLearn; }

    public List<TagPerformance> getTagPerformances() { return tagPerformances; }
    public void setTagPerformances(List<TagPerformance> tagPerformances) { this.tagPerformances = tagPerformances; }

    public List<SuggestedProblem> getOverallSuggestedProblems() { return overallSuggestedProblems; }
    public void setOverallSuggestedProblems(List<SuggestedProblem> overallSuggestedProblems) { this.overallSuggestedProblems = overallSuggestedProblems; }

    public List<String> getGlobalActionPlanRoadmap() { return globalActionPlanRoadmap; }
    public void setGlobalActionPlanRoadmap(List<String> globalActionPlanRoadmap) { this.globalActionPlanRoadmap = globalActionPlanRoadmap; }
}