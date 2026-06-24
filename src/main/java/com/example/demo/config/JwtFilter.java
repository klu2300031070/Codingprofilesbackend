package com.example.demo.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.JwtService;
import com.example.demo.service.MyUserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtservice;
    
    @Autowired
    ApplicationContext context;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        
        // 1. Safely check and extract the token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // 💡 ADDED .trim() here to strip out all hidden whitespaces or newline breaks!
            token = authHeader.substring(7).trim(); 
            
            // Safe fallback: Make sure the token isn't completely empty after trimming
            if (!token.isEmpty()) {
                username = jwtservice.extractUserName(token);
            }
        }
        
        // 2. Authenticate the user if the token is valid
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userdetails = context.getBean(MyUserService.class).loadUserByUsername(username);
            
            if (jwtservice.validateToken(token, userdetails)) {
                UsernamePasswordAuthenticationToken authtoken =
                        new UsernamePasswordAuthenticationToken(userdetails, null, userdetails.getAuthorities());
                authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authtoken);
            }
        }
        
        // 3. Continue filter chain execution
        filterChain.doFilter(request, response);
    }
}