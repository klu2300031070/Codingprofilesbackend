package com.example.demo.dto;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CodeforcesSubmission {
    private int id;
    private int contestId;
    private String verdict; // e.g., "OK" for correct, "WRONG_ANSWER"
    private Problem problem;

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getContestId() {
		return contestId;
	}

	public void setContestId(int contestId) {
		this.contestId = contestId;
	}

	public String getVerdict() {
		return verdict;
	}

	public void setVerdict(String verdict) {
		this.verdict = verdict;
	}

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	@Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Problem {
        private String name;
        private String index; // e.g., "A", "B", "C"
        private Integer rating; // Problem difficulty level (e.g., 1200)
        private List<String> tags; // CRUCIAL: ["dp", "greedy", "graphs"]
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getIndex() {
			return index;
		}
		public void setIndex(String index) {
			this.index = index;
		}
		public Integer getRating() {
			return rating;
		}
		public void setRating(Integer rating) {
			this.rating = rating;
		}
		public List<String> getTags() {
			return tags;
		}
		public void setTags(List<String> tags) {
			this.tags = tags;
		}
    }
}
