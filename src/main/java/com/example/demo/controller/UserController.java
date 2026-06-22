package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.Users;
import com.example.demo.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService us;
	
	private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);
	
	@PostMapping("/register")
	public Users register(@RequestBody Users u) {
		return us.register(u);
	}
	@PostMapping("/signin")
	public String login(@RequestBody Users u) {
		return us.verify(u);
	}
}
