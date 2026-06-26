package com.example.demo.service;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.model.Users;

public class UserPrincipal implements UserDetails {
    
    /**w
	 * 
	 */
	private final Users user; 

    // 🔥 FIX: Changed from private to public so UserService can instantiate it
    public UserPrincipal(Users u) {
        this.user = u;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	return Collections.singleton(new SimpleGrantedAuthority("ROLE_"+user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
}