package com.example.demo.dto.leetcode;


import java.util.List;

public class TagPerformance {
    private String tagName;
    private String masteryLevel; // "Beginner", "Intermediate", "Pro"
    private int score;           // Out of 10
    private String analysis;     // Brief explanation of why they got this score
    private List<SuggestedProblem> suggestedProblems; // Problem path suggestions inside this topic

    // Getters and Setters
    public String getTagName() { return tagName; }
    public void setTagName(String tagName) { this.tagName = tagName; }

    public String getMasteryLevel() { return masteryLevel; }
    public void setMasteryLevel(String masteryLevel) { this.masteryLevel = masteryLevel; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getAnalysis() { return analysis; }
    public void setAnalysis(String analysis) { this.analysis = analysis; }

    public List<SuggestedProblem> getSuggestedProblems() { return suggestedProblems; }
    public void setSuggestedProblems(List<SuggestedProblem> suggestedProblems) { this.suggestedProblems = suggestedProblems; }
}
