package com.example.demo.service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UserResponse;
import com.example.demo.model.Users;
import com.example.demo.repo.UserRepo;

@Service
public class UserService {

	@Autowired
	private JwtService jwt;
	
	@Autowired
	public UserRepo ur;
	
	@Autowired
	private AuthenticationManager authmanger;
	
	@Autowired
	private StringRedisTemplate redisTemplate; 

	@Autowired
	private BrevoApiService emailService; 
	
	private static final String OTP_PREFIX = "OTP:";
	
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

	public List<UserResponse> getAllUsers() {
		try {
		List<UserResponse>res= ur.findAll()
	            .stream()
	            .map(user -> new UserResponse(
	                    user.getId(),
	                    user.getUsername(),
	                    user.getRole()
	            ))
	            .toList();
			return res;
		}catch(Exception e) {
			e.printStackTrace();

			throw new RuntimeException("Unable to fetch users.", e);
		}
		
	}
	public Map<String, Object> initiateLoginVerify(Users u) {
		Map<String, Object> response = new HashMap<>();
	    try {
	        System.out.println("Login Step 1 Attempt: " + u.getUsername());

	        // 1. Validate Username & Password against the Database
	        Authentication auth = authmanger.authenticate(
	                new UsernamePasswordAuthenticationToken(u.getUsername(), u.getPassword()));

	        if (auth.isAuthenticated()) {
	            
	            // 1. Fetch the user profile from the database
	            Users dbUser = ur.findByUsername(u.getUsername());
	            
	            // 2. 🔥 SAFETY CHECK: Verify that the user record has a valid email address assigned to it
	            if (dbUser == null || dbUser.getEmail() == null || dbUser.getEmail().trim().isEmpty()) {
	                System.out.println("❌ Auth Stopped: The account '" + u.getUsername() + "' has no linked email address.");
	                response.put("status", "ERROR");
	                response.put("message", "Authentication cannot proceed because no email address is linked to this account.");
	                return response;
	            }

	            // 3. Generate a 6-digit verification code string
	            String otpCode = String.format("%06d", new Random().nextInt(999999));

	            // 4. Stash code in Redis cache for exactly 5 minutes
	            redisTemplate.opsForValue().set(
	                    "OTP:" + u.getUsername(), 
	                    otpCode, 
	                    Duration.ofMinutes(5)
	            );

	            // 5. Safely invoke mail delivery without running into a NullPointerException
	            emailService.sendOtpEmail(dbUser.getEmail(), otpCode);

	            response.put("status", "OTP_SENT");
	            response.put("message", "Verification code dispatched to email. Valid for 5 mins.");
	            return response;
	        }
	        
	        response.put("status", "UNAUTHORIZED");
	        return response;

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("status", "ERROR");
	        response.put("message", e.getMessage());
	        return response;
	    }
	}

	/**
	 * STEP 2: Verify the user submitted OTP token and issue final JWT
	 */
	public Map<String, Object> completeOtpVerification(String username, String userCode) {
		Map<String, Object> response = new HashMap<>();
		String redisKey = OTP_PREFIX + username;
		String savedOtp = redisTemplate.opsForValue().get(redisKey);

		if (savedOtp == null) {
			response.put("status", "EXPIRED");
			response.put("message", "The verification code has expired or was never requested.");
			return response;
		}

		if (!savedOtp.equals(userCode)) {
			response.put("status", "INVALID");
			response.put("message", "Incorrect verification code. Please check your email inbox.");
			return response;
		}

		// Success! Erase the temporary OTP token out of Redis immediately to prevent replay hacks
		redisTemplate.delete(redisKey);

		// Issue the final access token
		String token = jwt.generateToken(username);
		response.put("status", "SUCCESS");
		response.put("token", token);
		return response;
	}

	public boolean userExists(Users user) {
		return ur.findByUsername(user.getUsername())!=null;
	}
}
