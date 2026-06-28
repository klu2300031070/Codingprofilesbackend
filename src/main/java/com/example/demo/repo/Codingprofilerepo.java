package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Codingprofiles;


public interface Codingprofilerepo extends JpaRepository<Codingprofiles,Integer> {
	Codingprofiles findByCfusername(String cfusername);
	Codingprofiles findByLcusername(String lcusername);

}
