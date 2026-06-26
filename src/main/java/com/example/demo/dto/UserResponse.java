package com.example.demo.dto;

public class UserResponse {

    private int id;
    private String username;
    private String role;
	public UserResponse(int id2, String username2, String role2) {
		this.id=id2;
		this.username=username2;
		this.role=role2;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

}