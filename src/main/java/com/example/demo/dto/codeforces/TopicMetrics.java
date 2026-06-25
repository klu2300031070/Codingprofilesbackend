package com.example.demo.dto.codeforces;

public class TopicMetrics {

    private int solvedCount;

    public double getScoreOutToTen() {
		return scoreOutToTen;
	}

	public void setScoreOutToTen(double scoreOutToTen) {
		this.scoreOutToTen = scoreOutToTen;
	}

	private double avgRating;

    private int maxRating;

    private int recentSolved;
    private double scoreOutToTen;

    public int getSolvedPercentage() {
		return solvedPercentage;
	}

	public void setSolvedPercentage(int solvedPercentage) {
		this.solvedPercentage = solvedPercentage;
	}

	private double strengthScore;

    private String proficiencyLevel;
    private int solvedPercentage;

    public int getSolvedCount() {
        return solvedCount;
    }

    public void setSolvedCount(int solvedCount) {
        this.solvedCount = solvedCount;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public int getMaxRating() {
        return maxRating;
    }

    public void setMaxRating(int maxRating) {
        this.maxRating = maxRating;
    }

    public int getRecentSolved() {
        return recentSolved;
    }

    public void setRecentSolved(int recentSolved) {
        this.recentSolved = recentSolved;
    }

    public double getStrengthScore() {
        return strengthScore;
    }

    public void setStrengthScore(double strengthScore) {
        this.strengthScore = strengthScore;
    }

    public String getProficiencyLevel() {
        return proficiencyLevel;
    }

    public void setProficiencyLevel(String proficiencyLevel) {
        this.proficiencyLevel = proficiencyLevel;
    }
}