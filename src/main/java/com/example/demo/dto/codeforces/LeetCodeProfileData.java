package com.example.demo.dto.codeforces;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LeetCodeProfileData {
    private MatchedUser matchedUser;
    private UserContestRanking userContestRanking;
    private List<UserContestRankingHistory> userContestRankingHistory;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MatchedUser {
        private TagProblemCounts tagProblemCounts;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TagProblemCounts {
        private List<TagCount> fundamental;
        private List<TagCount> intermediate;
        private List<TagCount> advanced;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TagCount {
        private String tagName;
        private String tagSlug;
        private int problemsSolved; // CRUCIAL for your analytics tracking
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserContestRanking {
        private int attendedContestsCount;
        private double rating;
        private int globalRanking;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserContestRankingHistory {
        private boolean attended;
        private int ranking;
        private double rating;
        private Contest contest;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Contest {
            private String title;
            private long startTime;
        }
    }
}
