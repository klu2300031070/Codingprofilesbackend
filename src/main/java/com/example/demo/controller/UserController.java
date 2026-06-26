package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserResponse;
import com.example.demo.model.Users;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RestController
public class UserController {

    @Autowired
    private UserService us;
    
    

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Users user) {

        if (us.userExists(user)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username already exists.");
        }

        System.out.println("Register API Hit");

        Users registeredUser = us.register(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(registeredUser);
    }

   /* @PostMapping("/login")
    public String login(@RequestBody Users request) {
        return us.verify(request);
    }*/
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Users request) {
        Map<String, Object> result = us.initiateLoginVerify(request);
        
        if ("UNAUTHORIZED".equals(result.get("status"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
        return ResponseEntity.ok(result);
    }

    // Step 2: Validate the OTP token sent via mail delivery channels
    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody Map<String, String> requestData) {
        String username = requestData.get("username");
        String otpCode = requestData.get("otpCode");

        if (username == null || otpCode == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Missing username or otpCode fields."));
        }

        Map<String, Object> result = us.completeOtpVerification(username, otpCode);

        if (!"SUCCESS".equals(result.get("status"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        return ResponseEntity.ok(result);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        return us.getAllUsers();
    }
}