package com.example.demo.service.codeforces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.codeforces.CodeforcesSubmission;
import com.example.demo.dto.codeforces.TopicMetrics;
import com.example.demo.dto.codeforces.UserAnalysisReport;
import com.example.demo.dto.codeforces.CodeforcesSubmission.Problem;

import java.util.*;

@Service
public class CodeforcesService {

	@Autowired
	private CodeforcesDataGatewayService cds;

	// Static map holding the difficulty tier classification for all 37 official Codeforces tags
	private static final Map<String, Integer> TOPIC_TIERS = new HashMap<>();

	static {
		// Tier 1: Core Basics / Implementation
		TOPIC_TIERS.put("implementation", 1);
		TOPIC_TIERS.put("brute force", 1);

		// Tier 2: Foundational Mathematics & Processing
		TOPIC_TIERS.put("math", 2);
		TOPIC_TIERS.put("strings", 2);
		TOPIC_TIERS.put("sortings", 2);
		TOPIC_TIERS.put("number theory", 2);

		// Tier 3: Classic Greedy & Traversal
		TOPIC_TIERS.put("greedy", 3);
		TOPIC_TIERS.put("two pointers", 3);
		TOPIC_TIERS.put("binary search", 3);
		TOPIC_TIERS.put("ternary search", 3);

		// Tier 4: Intermediate Structural Logic
		TOPIC_TIERS.put("constructive algorithms", 4);
		TOPIC_TIERS.put("bitmasks", 4);
		TOPIC_TIERS.put("hashing", 4);
		TOPIC_TIERS.put("geometry", 4);

		// Tier 5: Dynamic Programming & Probability
		TOPIC_TIERS.put("dp", 5);
		TOPIC_TIERS.put("combinatorics", 5);
		TOPIC_TIERS.put("probabilities", 5);

		// Tier 6: Graphs & Network Foundations
		TOPIC_TIERS.put("graphs", 6);
		TOPIC_TIERS.put("trees", 6);
		TOPIC_TIERS.put("dfs and similar", 6);
		TOPIC_TIERS.put("shortest paths", 6);

		// Tier 7: Advanced Structures & Mathematics
		TOPIC_TIERS.put("data structures", 7);
		TOPIC_TIERS.put("dsu", 7);
		TOPIC_TIERS.put("matrices", 7);
		TOPIC_TIERS.put("flows", 7);
		TOPIC_TIERS.put("graph matchings", 7);
		TOPIC_TIERS.put("string suffix structures", 7);
		TOPIC_TIERS.put("expression parsing", 7);

		// Tier 8: Special Heavy Algorithms / Elite Mastery
		TOPIC_TIERS.put("games", 8);
		TOPIC_TIERS.put("meet-in-the-middle", 8);
		TOPIC_TIERS.put("fft", 8);
		TOPIC_TIERS.put("divide and conquer", 8);
		TOPIC_TIERS.put("interactive", 8);
		TOPIC_TIERS.put("schedules", 8);
		TOPIC_TIERS.put("chinese remainder theorem", 8);
		TOPIC_TIERS.put("2-sat", 8);
		TOPIC_TIERS.put("*special", 8);
	}
	
	/**
	 * Computes the relative percentage share of unique correct solutions grouped by tag.
	 */
    public Map<String, Integer> calculatetotalpercentagesolvedofeachtag(String handle) {
        List<CodeforcesSubmission> submissions = cds.getUserFullProfile(handle);
        int totalsolved = gettotalsubmmited(submissions);
        Map<String, Integer> tagCounts = calculateTopicPerformance(handle);
        
        java.util.LinkedHashMap<String, Integer> res = new java.util.LinkedHashMap<>();
        
        if (totalsolved == 0 || tagCounts.containsKey(handle)) {
            return res; 
        }

        List<Map.Entry<String, Integer>> entryList = new java.util.ArrayList<>(tagCounts.entrySet());
        entryList.sort((a, b) -> {
            int tierA = TOPIC_TIERS.getOrDefault(a.getKey().toLowerCase().trim(), 9);
            int tierB = TOPIC_TIERS.getOrDefault(b.getKey().toLowerCase().trim(), 9);
            
            if (tierA != tierB) {
                return Integer.compare(tierA, tierB); // Primary: Complexity Tier Easy -> Hard
            }
            return a.getKey().compareTo(b.getKey()); // Secondary: Alphabetical
        });

        for (Map.Entry<String, Integer> entry : entryList) {
            String tag = entry.getKey();
            int count = entry.getValue();
            int percentage = (int) Math.round(((double) count / totalsolved) * 100);
            res.put(tag, percentage);
        }
        
        return res;
    }

    /**
	 * Builds standard historical wrapper output profile maps.
	 */
    public Map<String, Object> getUserFullProfile(String handle) {
    	Map<String, Object> res = new HashMap<>();
    	List<CodeforcesSubmission> data = cds.getUserFullProfile(handle);
    	res.put("Submissions ", data);
    	res.put(handle + " Total Submitted", gettotalsubmmited(data));
    	return res;
    }
    
    /**
	 * Aggregates total verified unique accepted question entries.
	 */
    public static int gettotalsubmmited(List<CodeforcesSubmission> ls) {
    	HashSet<String> hs = new HashSet<>();
    	for (CodeforcesSubmission i : ls) {
    		if (i != null && i.getVerdict() != null && "OK".equals(i.getVerdict()) && i.getProblem() != null) {
    			hs.add(i.getProblem().getName());
    		}
    	}
    	return hs.size();
    }

    /**
	 * Maps overall problem tags counters safely.
	 */
    public Map<String, Integer> calculateTopicPerformance(String handle) {
        Map<String, Integer> hm = new HashMap<>();
        Set<String> hs = new HashSet<>();
        List<CodeforcesSubmission> data = cds.getUserFullProfile(handle);
        
        if (data != null) {
            for (CodeforcesSubmission i : data) {
                if (i != null && i.getProblem() != null && i.getProblem().getTags() != null 
                		&& i.getProblem().getRating() != null && "OK".equals(i.getVerdict()) 
                		&& !hs.contains(i.getProblem().getName())) {
                    
                	hs.add(i.getProblem().getName());
                    for (String j : i.getProblem().getTags()) {
                        if (j != null) {
                            hm.put(j, hm.getOrDefault(j, 0) + 1);
                        }
                    }
                }
            }
        } else {
        	hm.put(handle, 404);
        }
        return hm;
    }

    /**
	 * Generates deep core business performance metrics reports for analytics tracking dashboards.
	 */
    public UserAnalysisReport generateAdvancedAnalysis(String handle) {
        List<CodeforcesSubmission> submissions = cds.getUserFullProfile(handle);
        UserAnalysisReport report = new UserAnalysisReport();

        if (submissions == null || submissions.isEmpty()) {
            return report;
        }

        Set<String> solvedProblems = new HashSet<>();
        Map<String, TopicMetrics> topicMap = new HashMap<>();

        int totalRating = 0;
        int highestRating = 0;
        int solvedCount = 0;

        // Pass 1: Parse and aggregate baseline metrics
        for (CodeforcesSubmission submission : submissions) {
            if (submission == null || submission.getProblem() == null || submission.getVerdict() == null || !"OK".equals(submission.getVerdict())) {
                continue;
            }

            Problem problem = submission.getProblem();
            if (problem.getName() == null || solvedProblems.contains(problem.getName())) {
                continue;
            }

            // Lock problem signature to correctly increments total tally (including unrated anomalies)
            solvedProblems.add(problem.getName());
            solvedCount++;

            // Isolated rating check block to compute statistical values securely
            if (problem.getRating() != null) {
                int rating = problem.getRating();
                totalRating += rating;
                highestRating = Math.max(highestRating, rating);

                if (problem.getTags() != null) {
                    for (String tag : problem.getTags()) {
                        TopicMetrics metric = topicMap.computeIfAbsent(tag, k -> new TopicMetrics());
                        metric.setSolvedCount(metric.getSolvedCount() + 1);
                        metric.setAvgRating(metric.getAvgRating() + rating);
                        metric.setMaxRating(Math.max(metric.getMaxRating(), rating));
                    }
                }
            }
        }

        // Pass 2: Calculate averages, structural weights, percentages, and normalized scores out of 10
        for (Map.Entry<String, TopicMetrics> entry : topicMap.entrySet()) {
            TopicMetrics metric = entry.getValue();

            double avg = metric.getAvgRating() / metric.getSolvedCount();
            metric.setAvgRating(avg);

            // Computational Strength Score Formula
            double strengthScore = (metric.getSolvedCount() * 0.4)
                    + ((avg / 100.0) * 0.4)
                    + ((metric.getMaxRating() / 100.0) * 0.2);

            metric.setStrengthScore(Math.round(strengthScore * 100.0) / 100.0);

            // Compute dynamic tag distribution percentage out of all global unique solves
            int percentage = solvedCount == 0 ? 0 : (int) Math.round(((double) metric.getSolvedCount() / solvedCount) * 100);
            metric.setSolvedPercentage(percentage);

            // Min-Max Normalization: Convert raw strength scores to a clear score out of 10
            // Capping at a value of 30.0 for a perfect 10/10 setup
            double scaledScore = (strengthScore / 30.0) * 10.0;
            if (scaledScore > 10.0) {
                scaledScore = 10.0; 
            }
            metric.setScoreOutToTen(Math.round(scaledScore * 10.0) / 10.0);

            // Map Qualitative Labels
            if (strengthScore >= 25)
                metric.setProficiencyLevel("Expert");
            else if (strengthScore >= 15)
                metric.setProficiencyLevel("Advanced");
            else if (strengthScore >= 8)
                metric.setProficiencyLevel("Intermediate");
            else
                metric.setProficiencyLevel("Beginner");
        }

        // Calculate general average across rated solutions
        double overallAvg = solvedCount == 0 ? 0 : (double) totalRating / solvedCount;

        // Map global profile report domains
        report.setTotalSolved(solvedCount);
        report.setAverageProblemRating(overallAvg);
        report.setHighestProblemRating(highestRating);
        report.setTopicStats(topicMap);

        // Populate array lists using localized custom logic matrices
        report.setStrongTopics(getStrongTopics(topicMap));
        report.setWeakTopics(getWeakTopics(topicMap));
        report.setUnexploredTopics(getUnexploredTopics(topicMap));
        report.setRecommendedNextTopics(getRecommendedTopics(topicMap));
        report.setProfileLevel(determineProfileLevel(overallAvg));
        report.setEstimatedSkillBand(estimateSkillBand(overallAvg));

        return report;
    }

    private List<String> getStrongTopics(Map<String, TopicMetrics> topicMap) {
        return topicMap.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue().getStrengthScore(), a.getValue().getStrengthScore()))
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();
    }

    private List<String> getWeakTopics(Map<String, TopicMetrics> topicMap) {
        return topicMap.entrySet().stream()
                .filter(e -> e.getValue().getStrengthScore() < 8.0)
                .sorted((a, b) -> Double.compare(a.getValue().getStrengthScore(), b.getValue().getStrengthScore()))
                .map(Map.Entry::getKey)
                .toList();
    }

    private List<String> getUnexploredTopics(Map<String, TopicMetrics> topicMap) {
        return TOPIC_TIERS.keySet().stream()
                .filter(tag -> !topicMap.containsKey(tag))
                .sorted()
                .toList();
    }

    private List<String> getRecommendedTopics(Map<String, TopicMetrics> topicMap) {
        List<String> weak = getWeakTopics(topicMap);
        if (!weak.isEmpty()) {
            return weak.stream().limit(5).toList();
        }
        return getUnexploredTopics(topicMap).stream().limit(5).toList();
    }

    private String determineProfileLevel(double avgRating) {
        if (avgRating >= 2200) return "Master";
        if (avgRating >= 1900) return "Expert";
        if (avgRating >= 1600) return "Advanced";
        if (avgRating >= 1300) return "Intermediate";
        return "Beginner";
    }

    private String estimateSkillBand(double avgRating) {
        if (avgRating >= 2200) return "2100-2400";
        if (avgRating >= 1900) return "1800-2100";
        if (avgRating >= 1600) return "1500-1800";
        if (avgRating >= 1300) return "1200-1500";
        return "<1200";
    }
}