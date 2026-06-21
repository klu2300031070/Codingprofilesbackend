package com.example.demo.dto;

import java.util.List;
import java.util.Map;

public class UserAnalysisReport {

    private Map<String, TopicMetrics> topicStats;

    private List<String> strongTopics;

    private List<String> weakTopics;

    private List<String> unexploredTopics;

    private List<String> recommendedNextTopics;

    private int totalSolved;

    private double averageProblemRating;

    private int highestProblemRating;

    private String profileLevel;

    private String estimatedSkillBand;

    public Map<String, TopicMetrics> getTopicStats() {
        return topicStats;
    }

    public void setTopicStats(Map<String, TopicMetrics> topicStats) {
        this.topicStats = topicStats;
    }

    public List<String> getStrongTopics() {
        return strongTopics;
    }

    public void setStrongTopics(List<String> strongTopics) {
        this.strongTopics = strongTopics;
    }

    public List<String> getWeakTopics() {
        return weakTopics;
    }

    public void setWeakTopics(List<String> weakTopics) {
        this.weakTopics = weakTopics;
    }

    public List<String> getUnexploredTopics() {
        return unexploredTopics;
    }

    public void setUnexploredTopics(List<String> unexploredTopics) {
        this.unexploredTopics = unexploredTopics;
    }

    public List<String> getRecommendedNextTopics() {
        return recommendedNextTopics;
    }

    public void setRecommendedNextTopics(List<String> recommendedNextTopics) {
        this.recommendedNextTopics = recommendedNextTopics;
    }

    public int getTotalSolved() {
        return totalSolved;
    }

    public void setTotalSolved(int totalSolved) {
        this.totalSolved = totalSolved;
    }

    public double getAverageProblemRating() {
        return averageProblemRating;
    }

    public void setAverageProblemRating(double averageProblemRating) {
        this.averageProblemRating = averageProblemRating;
    }

    public int getHighestProblemRating() {
        return highestProblemRating;
    }

    public void setHighestProblemRating(int highestProblemRating) {
        this.highestProblemRating = highestProblemRating;
    }

    public String getProfileLevel() {
        return profileLevel;
    }

    public void setProfileLevel(String profileLevel) {
        this.profileLevel = profileLevel;
    }

    public String getEstimatedSkillBand() {
        return estimatedSkillBand;
    }

    public void setEstimatedSkillBand(String estimatedSkillBand) {
        this.estimatedSkillBand = estimatedSkillBand;
    }
}