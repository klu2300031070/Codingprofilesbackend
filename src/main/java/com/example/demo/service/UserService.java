package com.example.demo.service;

import java.security.AuthProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Users;
import com.example.demo.repo.UserRepo;

@Service
public class UserService {

	@Autowired
	private JwtService jwt;
	
	@Autowired
	public UserRepo ur;
	
	@Autowired
	private AuthenticationManager authmanger;
	
	private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(12);
	
	public Users register(Users u) {
		u.setPassword(encoder.encode( u.getPassword()));
		ur.save(u);
		return u;
	}

	public String verify(Users u) {

	    try {

	        System.out.println("Login Attempt");
	        System.out.println(u.getUsername());

	        Authentication auth = authmanger.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                        u.getUsername(),
	                        u.getPassword()));

	        System.out.println("Authenticated = " + auth.isAuthenticated());

	        return jwt.generateToken(u.getUsername());

	    } catch (Exception e) {

	        e.printStackTrace();

	        return e.getClass().getName() + " : " + e.getMessage();
	    }
	}
}
