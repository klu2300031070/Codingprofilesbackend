package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableCaching
@EnableMethodSecurity
public class PracticeProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(PracticeProjectApplication.class, args);
		System.out.println("Project is running");
		
	}

}
