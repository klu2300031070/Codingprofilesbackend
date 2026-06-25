package com.example.demo.dto.codeforces;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CodeforcesSubmission {
    
    // Root Level Fields
    private int id;
    private int contestId;
    private long creationTimeSeconds; // Maps perfectly to 1775314522
    private int relativeTimeSeconds;
    private String programmingLanguage; // e.g., "Java 8"
    private String verdict; // e.g., "OK", "WRONG_ANSWER"
    private String testset; // e.g., "TESTS", "PRETESTS"
    private int passedTestCount;
    private long timeConsumedMillis;
    private long memoryConsumedBytes;
    
    // Nested Domain Objects
    private Problem problem;
    private Author author;
    
    // --- NESTED CLASSES ---

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Problem {
        private int contestId;
        private String index; // "A", "B", "C"
        private String name;  // "The 67th Permutation Problem"
        private String type;  // "PROGRAMMING"
        private Integer points; // Can be null, using Integer object wrapper
        private Integer rating; // e.g., 800
        private List<String> tags; // ["constructive algorithms", "greedy", "math"]
        
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Author {
        private int contestId;
        private String participantId;
        private String participantType; // "CONTESTANT", "PRACTICE"
        private boolean ghost;
        private Integer room; // Using Integer wrapper since it can be absent/null
        private long startTimeSeconds;
        private List<Member> members;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Member {
        private String handle; // "klu2300031070"
        
    }
}