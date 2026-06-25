package com.example.demo.dto.codeforces;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CodeforcesUserInfo {
    private String handle;
    private int rating;
    private int maxRating;
    private String rank;
    private String maxRank;
    private String avatar;
	public String getHandle() {
		return handle;
	}
	public void setHandle(String handle) {
		this.handle = handle;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public int getMaxRating() {
		return maxRating;
	}
	public void setMaxRating(int maxRating) {
		this.maxRating = maxRating;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getMaxRank() {
		return maxRank;
	}
	public void setMaxRank(String maxRank) {
		this.maxRank = maxRank;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
}
