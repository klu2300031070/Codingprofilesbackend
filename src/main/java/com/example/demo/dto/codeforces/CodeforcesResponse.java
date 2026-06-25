package com.example.demo.dto.codeforces;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CodeforcesResponse<T> {
    private String status; // Will be "OK" or "FAILED"
    private String comment; // Error details if failed
    private T result;      // Can be a List of Submissions or List of UserInfo
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
}