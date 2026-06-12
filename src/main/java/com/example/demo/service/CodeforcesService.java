package com.example.demo.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable; // <-- Added Import

import org.springframework.core.ParameterizedTypeReference;

import org.springframework.http.HttpMethod;

import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;



import com.example.demo.dto.CodeforcesResponse;

import com.example.demo.dto.CodeforcesSubmission;

import com.example.demo.dto.CodeforcesSubmission.Problem;

import com.example.demo.dto.CodeforcesUserInfo;



import java.util.Collections;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import java.util.Map;
import java.util.Set;



@Service
public class CodeforcesService {
	@Autowired
	private CodeforcesDataGatewayService cds;




   
    public  Map<String, Object> getUserFullProfile(String handle) {
    	Map<String, Object> res=new HashMap<>();
    	List<CodeforcesSubmission> data=cds.getUserFullProfile(handle);
    	res.put("Submissions ",data);
    	res.put(handle+" Total Submitted",gettotalsubmmited(data));
    	return res;
    }
    
    
    public static int  gettotalsubmmited(List<CodeforcesSubmission> ls) {
    	HashSet<String> hs=new HashSet<>();
    	for(CodeforcesSubmission i:ls) {
    		if(i.getVerdict().equals("OK")) {
    			hs.add(i.getProblem().getName());
    		}
    	}
    	return hs.size();
    }



    // Cache rule: Maps to "codeforcesTopicPerformance" bucket using handle as the key.

    // If not in Redis, runs the logic, creates the map, and pushes to Redis for 24 hours.

   
    public Map<String, Integer> calculateTopicPerformance(String handle) {
        //System.out.println("⚠️ Cache MISS - Running Topic Analysis Analytics for: " + handle);
        Map<String, Integer> hm = new HashMap<>();
        Set<String> hs=new HashSet<>();
        List<CodeforcesSubmission> data=cds.getUserFullProfile(handle);
                if (data != null) {
                    for (CodeforcesSubmission i : data) {
                        // DEFENSIVE FIX: Guard against missing problem blocks or missing tag structures
                        if (i != null && i.getProblem() != null && i.getProblem().getTags() != null&&i.getProblem().getRating()!=null&&i.getVerdict().equals("OK")&&!hs.contains(i.getProblem().getName())) {
                        	hs.add(i.getProblem().getName());
                            for (String j : i.getProblem().getTags()) {
                                if (j != null) {
                                    hm.put(j, hm.getOrDefault(j, 0) + i.getProblem().getRating());
                                }
                                
                            }
                        }
                    }
                }else {
                	hm.put(handle,404);
                }
        return hm;
    }

}