package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Users; // 🔥 FIX: Import your own entity, not Spring's User class
import com.example.demo.repo.UserRepo;

@Service // 🔥 FIX: Added bean annotation so Spring finds it
public class MyUserService implements UserDetailsService {
    
    @Autowired
    private UserRepo ur;
    
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Users u = ur.findByUsername(username);

        System.out.println("DB Username : " + u.getUsername());
        System.out.println("DB Password : " + u.getPassword());

        return new UserPrincipal(u);
    }
}