package com.example.demo.dto.leetcode;


public class SuggestedProblem {
    private int stepOrder;        // 1, 2, 3... indicates the hierarchy order of solving
    private String problemTitle;
    private String difficulty;     // "Easy", "Medium", "Hard"
    private String leetCodeUrl;
    private String coreReasoning;  // Must mention why it fits the hierarchy sequence

    // Getters and Setters
    public int getStepOrder() { return stepOrder; }
    public void setStepOrder(int stepOrder) { this.stepOrder = stepOrder; }

    public String getProblemTitle() { return problemTitle; }
    public void setProblemTitle(String problemTitle) { this.problemTitle = problemTitle; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getLeetCodeUrl() { return leetCodeUrl; }
    public void setLeetCodeUrl(String leetCodeUrl) { this.leetCodeUrl = leetCodeUrl; }

    public String getCoreReasoning() { return coreReasoning; }
    public void setCoreReasoning(String coreReasoning) { this.coreReasoning = coreReasoning; }
}