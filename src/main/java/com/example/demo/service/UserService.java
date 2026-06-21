package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Users; // 🔥 FIX: Import your own entity, not Spring's User class
import com.example.demo.repo.UserRepo;

@Service // 🔥 FIX: Added bean annotation so Spring finds it
public class UserService implements UserDetailsService {
    
    @Autowired
    private UserRepo ur;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users u = ur.findByUsername(username);
        if (u == null) {
            System.out.println("User Not Found: " + username);
            throw new UsernameNotFoundException("User not found: " + username);
        }
        // 🔥 FIX: Pass the found database user to the principal wrapper
        return new UserPrincipal(u);
    }
}