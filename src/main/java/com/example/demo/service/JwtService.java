package com.example.demo.service;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.google.common.primitives.Bytes;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	static String secrectkey="";
	public JwtService() {
		try {
			KeyGenerator key=KeyGenerator.getInstance("HmacSHA256");
			SecretKey sk=key.generateKey();
			secrectkey=Base64.getEncoder().encodeToString(sk.getEncoded());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String generateToken(String username) {
		
		
		Map<String,Object> claims=new HashMap<>();
		return Jwts.builder()
				.claims()
				.add(claims)
				.subject(username)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis()+60*60*30))
				.and()
				.signWith(getkey())
				.compact();
	}

	private Key getkey() {
		// TODO Auto-generated method stub
		byte[] keybytes=Decoders.BASE64.decode(secrectkey);
		return Keys.hmacShaKeyFor(keybytes);
	}
}
